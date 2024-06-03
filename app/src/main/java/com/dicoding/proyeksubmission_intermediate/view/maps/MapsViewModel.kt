package com.dicoding.proyeksubmission_intermediate.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.FetchResult
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<FetchResult<List<ListStoryItem>>>()
    val stories: LiveData<FetchResult<List<ListStoryItem>>> = _stories

    fun getListStoriesWithLocation() {
        _stories.value = FetchResult.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = repository.getUserToken()
                val result = repository.getListStoriesWithLocation(token)
                _stories.postValue(result)
            } catch (e: Exception) {
                _stories.postValue(FetchResult.Error(e))
            }
        }
    }
}