package com.submission.zelsis.repository

import com.google.gson.Gson
import com.submission.zelsis.data.local.preference.UserPreference
import com.submission.zelsis.data.remote.retrofit.ApiService
import com.submission.zelsis.data.remote.response.LoginResponse
import com.submission.zelsis.data.remote.retrofit.ApiConfig
import com.submission.zelsis.model.UserModel
import com.submission.zelsis.util.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
){

    suspend fun saveSession(user: UserModel){
        userPreference.logout()
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val login = apiService.login(email, password)
            Result.Success(login)
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, LoginResponse::class.java)
            Result.Error(response, "There is an error with server respond")
        } catch (e: Exception){
            Result.Error(null, e.message.toString())
        }
    }

    suspend fun logout(){
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService)
        }.also { instance = it }
    }

}