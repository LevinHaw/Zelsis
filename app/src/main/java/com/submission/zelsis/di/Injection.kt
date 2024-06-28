package com.submission.zelsis.di

import android.content.Context
import com.submission.zelsis.data.local.preference.UserPreference
import com.submission.zelsis.data.local.preference.dataStore
import com.submission.zelsis.data.remote.retrofit.ApiConfig
import com.submission.zelsis.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val userModel = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(userModel.token)
        return UserRepository.getInstance(pref, apiService)
    }

}