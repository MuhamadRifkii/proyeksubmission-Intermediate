package com.dicoding.proyeksubmission_intermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.FetchResult
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<FetchResult<List<ListStoryItem>>>()
    val stories: LiveData<FetchResult<List<ListStoryItem>>> = _stories

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getListStories() {
        _stories.value = FetchResult.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val token = repository.getUserToken()
                val result = repository.getListStories(token)
                _stories.postValue(result)
            } catch (e: Exception) {
                _stories.postValue(FetchResult.Error(e))
            }
        }
    }

    //TODO Add ability to logout account
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}