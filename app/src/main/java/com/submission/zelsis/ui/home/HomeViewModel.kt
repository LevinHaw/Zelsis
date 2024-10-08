package com.submission.zelsis.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.zelsis.data.remote.response.ListStoryItem
import com.submission.zelsis.data.repository.StoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val storyRepository: StoryRepository
): ViewModel() {

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _name = MutableLiveData<String?>()
    val name: MutableLiveData<String?> = _name

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStory().cachedIn(viewModelScope)

    fun getName(){
        viewModelScope.launch {
            storyRepository.getSession().collectLatest { userModel ->
                _name.value = userModel.name
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}