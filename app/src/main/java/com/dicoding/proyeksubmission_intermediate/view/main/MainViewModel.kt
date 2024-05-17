package com.dicoding.proyeksubmission_intermediate.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.FetchResult
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.api.ApiConfig
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.ListStoryItem
import com.dicoding.proyeksubmission_intermediate.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
                val response = ApiConfig.getApiService(token).getStories()
                val listStory = response.listStory
                _stories.postValue(FetchResult.Success(listStory))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    Gson().fromJson(errorBody, StoryResponse::class.java).message
                } else {
                    e.message()
                }
                _stories.postValue(FetchResult.Error(Throwable(errorMessage)))
            } catch (e: Exception) {
                _stories.postValue(FetchResult.Error(e))
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}