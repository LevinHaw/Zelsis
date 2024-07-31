package com.submission.zelsis.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.local.database.model.UserModel
import com.submission.zelsis.data.repository.UserRepository
import com.submission.zelsis.util.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result

    private val _userToken = MutableLiveData<UserModel?>()
    val userToken: MutableLiveData<UserModel?> = _userToken

    fun login(email: String, password: String){
        _isLoading.value = true

        viewModelScope.launch {
            when(val loginResponse = userRepository.login(email, password)){
                is Result.Success -> {
                    val name: String = loginResponse.data?.loginResult?.name.toString()
                    val token: String = loginResponse.data?.loginResult?.token.toString()

                    _isError.value = false
                    _result.value = loginResponse.message.toString()
                    _isLoading.value = false

                    _userToken.value = UserModel(email, name, token, isLogin = true)
                    saveSession(userToken.value!!)
                    userRepository.updateToken(token)
                }
                is Result.Error -> {
                    _isError.value = true
                    _result.value = loginResponse.message.toString()
                    _isLoading.value = false
                    Log.e(TAG, loginResponse.message.toString())
                }
                is Result.Loading -> {
                    _isError.value = false
                    _isLoading.value = true
                }
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            try {
                userRepository.saveSession(user)
                _isError.value = false
            } catch (e: Exception) {
                _isError.value = true
                _result.value = e.message
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}