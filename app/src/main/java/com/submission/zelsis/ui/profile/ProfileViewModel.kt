package com.submission.zelsis.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.repository.UserRepository
import com.submission.zelsis.util.Result
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    fun logout(){
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}