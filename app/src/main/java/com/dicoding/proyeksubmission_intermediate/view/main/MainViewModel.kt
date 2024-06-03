package com.dicoding.proyeksubmission_intermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _pagedStories = MutableLiveData<PagingData<ListStoryItem>>()
    val pagedStories: LiveData<PagingData<ListStoryItem>> = repository.getListStories().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getListStories() {
        viewModelScope.launch {
            repository.getListStories().observeForever { pagingData ->
                _pagedStories.postValue(pagingData)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}