package com.dicoding.proyeksubmission_intermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.proyeksubmission_intermediate.data.FetchResult
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<FetchResult<List<ListStoryItem>>>()
    val stories: LiveData<FetchResult<List<ListStoryItem>>> = _stories

    private val userToken = MutableLiveData<String>()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun setUserToken(token: String) {
        userToken.value = token
    }

//    fun getListStories() {
//        _stories.value = FetchResult.Loading
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val token = repository.getUserToken()
//                val result = repository.getListStories(token)
//                _stories.postValue(result)
//            } catch (e: Exception) {
//                _stories.postValue(FetchResult.Error(e))
//            }
//        }
//    }

    fun getStoriesPaged(token: String): Flow<PagingData<ListStoryItem>> {
        return repository.getStoriesPaged(token).cachedIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}