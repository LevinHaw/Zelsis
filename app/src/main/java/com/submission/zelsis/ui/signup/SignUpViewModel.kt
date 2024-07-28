package com.submission.zelsis.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.repository.UserRepository
import com.submission.zelsis.util.Result
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result


    fun register(name: String,email: String, password: String){
        _isLoading.value = true

        viewModelScope.launch {
            val signUpResponse = userRepository.signUp(name, email, password)

            when(signUpResponse){
                is Result.Success -> {
                    _isError.value = false
                    _result.value = signUpResponse.data?.message
                    _isLoading.value = false
                    Log.e(TAG, _result.toString())
                }
                is Result.Error -> {
                    _isError.value = true
                    _result.value = signUpResponse.data?.message
                    _isLoading.value = false
                    Log.e(TAG, _result.toString())
                }
                is Result.Loading -> {
                    _isError.value = false
                    _isLoading.value = true
                }
            }
        }
    }
    companion object {
        private const val TAG = "SignUpViewModel"
    }
}