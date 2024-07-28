package com.submission.zelsis.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.submission.zelsis.R
import com.submission.zelsis.data.local.database.StoryDatabase
import com.submission.zelsis.data.local.preference.UserPreference
import com.submission.zelsis.data.pagination.StoryRemoteMediator
import com.submission.zelsis.data.remote.response.ImageUploadResponse
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.data.remote.retrofit.ApiService
import com.submission.zelsis.util.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
){

    fun getAllStory(): LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, userPreference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun postStory(image: File, description: String): Result<ImageUploadResponse> {
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

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            userPreference: UserPreference, storyDatabase: StoryDatabase, apiService: ApiService
        ): StoryRepository = INSTANCE ?: synchronized(this){
            INSTANCE ?: StoryRepository(userPreference, storyDatabase, apiService)
        }.also { INSTANCE = it }
    }
}