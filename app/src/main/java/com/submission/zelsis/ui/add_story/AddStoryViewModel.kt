package com.submission.zelsis.ui.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.repository.StoryRepository
import com.submission.zelsis.util.Result
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun postStory(
        image: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    )
    {
        _isLoading.value = true

        viewModelScope.launch {
            when (val postStoryResponse = storyRepository.postStory(image, description, lat, lon)){
                is Result.Success -> {
                    _isError.value = false
                    _isLoading.value = false
                    _message.value = postStoryResponse.message
                }
                is Result.Loading -> {
                    _isError.value = false
                    _isLoading.value = true
                    _message.value = null
                }
                is Result.Error -> {
                    _isError.value = true
                    _isLoading.value = false
                    _message.value = postStoryResponse.message
                }
            }
        }
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }

}