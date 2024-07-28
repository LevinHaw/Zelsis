package com.submission.zelsis.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.data.repository.StoryRepository
import com.submission.zelsis.data.repository.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
): ViewModel() {

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _name = MutableLiveData<String?>()
    val name: MutableLiveData<String?> = _name

    init {
        getName()
    }

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStory().cachedIn(viewModelScope)

    private fun getName(){
        viewModelScope.launch {
            userRepository.getSession().collectLatest { userModel ->
                _name.value = userModel.email
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}