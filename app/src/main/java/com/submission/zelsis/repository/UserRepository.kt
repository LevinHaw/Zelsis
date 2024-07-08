package com.submission.zelsis.repository

import com.google.gson.Gson
import com.submission.zelsis.R
import com.submission.zelsis.data.local.preference.UserPreference
import com.submission.zelsis.data.remote.response.ImageUploadResponse
import com.submission.zelsis.data.remote.retrofit.ApiService
import com.submission.zelsis.data.remote.response.LoginResponse
import com.submission.zelsis.data.remote.response.RegisterResponse
import com.submission.zelsis.data.remote.response.StoryResponse
import com.submission.zelsis.data.remote.retrofit.ApiConfig
import com.submission.zelsis.model.UserModel
import com.submission.zelsis.util.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
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

    suspend fun getAllStory(): Result<StoryResponse>{
        return try {
            val getAllStory = apiService.getAllStory()
            Result.Success(getAllStory)
        } catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, StoryResponse::class.java)
            Result.Error(response, R.string.error_server_respond.toString())
        } catch (e: Exception){
            Result.Error(null, e.message.toString())
        }
    }

    suspend fun postStory(image: File, description: String): Result<ImageUploadResponse>{
        return try {
            val requestDesc = description.toRequestBody(
                "text/plain".toMediaType()
            )

            val requestImg = image.asRequestBody(
                "image/jpeg".toMediaType()
            )

            val multiPart = MultipartBody.Part.createFormData(
                "photo", image.name , requestImg
            )

            val postStory = apiService.postStory(multiPart, requestDesc)
            Result.Success(postStory)

        } catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ImageUploadResponse::class.java)
            Result.Error(response, R.string.error_server_respond.toString())
        } catch (e: Exception){
            Result.Error(null, e.message.toString())
        }
    }

    fun updateToken(token: String) {
        instance?.let {
            it.apiService = ApiConfig.getApiService(token)
        }
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