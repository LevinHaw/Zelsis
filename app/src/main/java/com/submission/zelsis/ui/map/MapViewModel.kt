package com.submission.zelsis.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.data.repository.StoryRepository
import com.submission.zelsis.util.Result
import kotlinx.coroutines.launch

class MapViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private var isDataLoaded = false

    init {
        getStoriesLocation()
    }

    private fun getStoriesLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            when (val response = storyRepository.getStoryWithLocation()) {
                is Result.Success-> {
                    _listStories.value = response.data?.listStory
                    _isError.value = false
                    _errorMessage.value = null
                    isDataLoaded = true
                    _isLoading.value = false
                }

                is Result.Error-> {
                    _isError.value = true
                    _errorMessage.value = response.message
                }

                is Result.Loading-> {
                    _isError.value = false
                    _errorMessage.value = null
                    _isLoading.value = true
                }
            }
        }
    }
}