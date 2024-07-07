package com.submission.zelsis.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.model.UserModel
import com.submission.zelsis.repository.UserRepository
import com.submission.zelsis.util.Result
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _name = MutableLiveData<String?>()
    val name: MutableLiveData<String?> = _name

    init {
        getName()
    }

    fun getAllStory(){
        _isLoading.value = true

        viewModelScope.launch {
            val storyResponse = userRepository.getAllStory()

            when (storyResponse){
                is Result.Success -> {
                    _isError.value = false
                    _stories.value = storyResponse.data?.listStory
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _isError.value = true
                    _message.value = storyResponse.message.toString()
                    _isLoading.value = false
                    Log.e(TAG, storyResponse.message.toString())
                }
                is Result.Loading -> {
                    _isError.value = false
                    _isLoading.value = true
                }
            }
        }
    }

    private fun getName(){
        viewModelScope.launch {
            userRepository.getSession().collectLatest { userModel ->
                _name.value = userModel.email
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}