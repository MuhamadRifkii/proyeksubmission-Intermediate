package com.dicoding.proyeksubmission_intermediate.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.api.ApiConfig
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.StoryUploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<String>()
    val uploadResult: LiveData<String> = _uploadResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun upload(image: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val token = repository.getUserToken()
                val apiService = ApiConfig.getApiService(token)
                val successResponse = apiService.uploadImage(image, description)
                _uploadResult.value = successResponse.message
                _isLoading.value = false
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, StoryUploadResponse::class.java)
                _uploadResult.value = errorResponse.message
                _isLoading.value = false
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}