package com.dicoding.proyeksubmission_intermediate.view.detail_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<Result<DetailStoryResponse>>()
    val detailStory: LiveData<Result<DetailStoryResponse>> = _detailStory

    fun loadDetailStory(storyId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory(storyId)
                _detailStory.value = Result.success(response)
            } catch (e: Exception) {
                // Handle error jika terjadi kesalahan saat memuat detail cerita
                e.printStackTrace()
            }
        }
    }
}