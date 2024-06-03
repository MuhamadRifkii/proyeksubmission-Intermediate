package com.dicoding.proyeksubmission_intermediate.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<String>()
    val uploadResult: LiveData<String> = _uploadResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun upload(image: MultipartBody.Part, description: RequestBody, lat : Float?, lon : Float?) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val successResponse = repository.uploadStory(image, description, lat, lon)
                _uploadResult.value = successResponse.message
                _isLoading.value = false
            } catch (e: Exception) {
                _uploadResult.value = e.message
                _isLoading.value = false
            }
        }
    }
}