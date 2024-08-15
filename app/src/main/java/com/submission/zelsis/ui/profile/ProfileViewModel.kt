package com.submission.zelsis.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.repository.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> = _name

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> = _email

    private val _imageUri = MutableLiveData<String?>()
    val imageUri: LiveData<String?> = _imageUri

    fun getName(){
        viewModelScope.launch {
            userRepository.getSession().collectLatest { userModel ->
                _name.value = userModel.name
            }
        }
    }

    fun getEmail(){
        viewModelScope.launch {
            userRepository.getSession().collectLatest { userModel ->
                _email.value = userModel.email
            }
        }
    }

    fun getImageUri() {
        viewModelScope.launch {
            userRepository.getSession().collectLatest { userModel ->
                _imageUri.value = userModel.imageUri // Add this line
            }
        }
    }

    fun saveImageUri(uri: String) { // Add this function
        viewModelScope.launch {
            userRepository.saveImageUri(uri)
        }
    }


    fun logout(){
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}