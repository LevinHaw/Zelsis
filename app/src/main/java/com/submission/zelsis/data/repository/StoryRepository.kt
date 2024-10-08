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
import com.submission.zelsis.data.local.database.model.UserModel
import com.submission.zelsis.data.local.preference.UserPreference
import com.submission.zelsis.data.pagination.StoryRemoteMediator
import com.submission.zelsis.data.remote.response.ImageUploadResponse
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.data.remote.response.StoryResponse
import com.submission.zelsis.data.remote.retrofit.ApiConfig
import com.submission.zelsis.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
) {

    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }

    suspend fun getStoryWithLocation(): Result<StoryResponse>{
        return try {
            val apiService = ApiConfig.getApiService(userPreference.getSession().first().token)
            val storyWithLocation = apiService.getStoryWithLocation()
            val response = storyWithLocation.listStory.filter {
                it.lat != null && it.lon != null
            }
            Result.Success(StoryResponse(listStory = response, error = false))
        } catch (e: Exception){
            Result.Error(null, e.message.toString())
        }
    }


    fun getAllStory(): LiveData<PagingData<ListStoryItem>> {
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

    suspend fun postStory(
        image: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Result<ImageUploadResponse> {
        return try {
            val apiService = ApiConfig.getApiService(userPreference.getSession().first().token)
            val postStory = apiService.postStory(image, description, lat, lon)
            Result.Success(postStory)

        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(error, ImageUploadResponse::class.java)
            Result.Error(response, R.string.error_server_respond.toString())
        } catch (e: Exception) {
            Result.Error(null, e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            userPreference: UserPreference, storyDatabase: StoryDatabase
        ): StoryRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: StoryRepository(userPreference, storyDatabase)
        }
    }
}