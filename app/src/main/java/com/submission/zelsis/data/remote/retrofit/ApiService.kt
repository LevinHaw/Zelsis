package com.submission.zelsis.data.remote.retrofit

import com.submission.zelsis.data.remote.response.LoginResponse
import com.submission.zelsis.data.remote.response.RegisterResponse
import com.submission.zelsis.data.remote.response.StoryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): LoginResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
    ): RegisterResponse


    @GET("stories")
    suspend fun getAllStory(
    ): StoryResponse

}