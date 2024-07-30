package com.submission.zelsis.data.repository

import com.google.gson.Gson
import com.submission.zelsis.R
import com.submission.zelsis.data.local.preference.UserPreference
import com.submission.zelsis.data.remote.response.ImageUploadResponse
import com.submission.zelsis.data.remote.retrofit.ApiService
import com.submission.zelsis.data.remote.response.LoginResponse
import com.submission.zelsis.data.remote.response.RegisterResponse
import com.submission.zelsis.data.remote.response.StoryResponse
import com.submission.zelsis.data.remote.retrofit.ApiConfig
import com.submission.zelsis.data.local.database.model.UserModel
import com.submission.zelsis.util.Result
import com.submission.zelsis.util.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
){

    suspend fun saveSession(user: UserModel){
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        wrapEspressoIdlingResource {
            return try {
                val login = apiService.login(email, password)
                Result.Success(login)
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()?.string()
                val response = Gson().fromJson(error, LoginResponse::class.java)
                Result.Error(response, R.string.error_server_respond.toString())
            } catch (e: Exception){
                Result.Error(null, e.message.toString())
            }
        }
    }

    suspend fun signUp(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val register = apiService.register(name,email, password)
            Result.Success(register)
        } catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, RegisterResponse::class.java)
            Result.Error(response, R.string.error_server_respond.toString())
        } catch (e: Exception) {
            Result.Error(null, e.message.toString())
        }
    }

    suspend fun logout(){
        userPreference.logout()
    }

    fun updateToken(token: String) {
        INSTANCE?.let {
            it.apiService = ApiConfig.getApiService(token)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserRepository(userPreference, apiService)
        }.also { INSTANCE = it }
    }

}