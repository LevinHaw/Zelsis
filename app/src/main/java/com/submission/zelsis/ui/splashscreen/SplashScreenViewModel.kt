package com.submission.zelsis.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.zelsis.model.UserModel
import com.submission.zelsis.repository.UserRepository

class SplashScreenViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    fun getSession(): LiveData<UserModel>{
        return userRepository.getSession().asLiveData()
    }
}