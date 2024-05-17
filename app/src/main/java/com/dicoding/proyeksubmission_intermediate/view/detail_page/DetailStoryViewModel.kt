package com.dicoding.proyeksubmission_intermediate.view.detail_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.DetailStoryResponse
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val detailStory: LiveData<Result<DetailStoryResponse>> = _detailStory

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun loadDetailStory(storyId: String) {
        viewModelScope.launch {
            val user = repository.getSession().firstOrNull()
            if (user != null) {
                try {
                    val response = repository.getDetailStory(storyId)
                    _detailStory.value = Result.success(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _detailStory.value = Result.failure(e)
                }
            } else {
                // Retry or handle the case where the session is not available
                _detailStory.value = Result.failure(Exception("Session not available"))
            }
        }
    }
}