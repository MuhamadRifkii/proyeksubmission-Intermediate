package com.dicoding.proyeksubmission_intermediate.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(name, email, password)
                _registerResult.value = result
            }
            catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorMessage = errorResponse?.let {
                    Gson().fromJson(it, RegisterResponse::class.java).message
                } ?: e.message()
                _registerResult.value = RegisterResponse(message = errorMessage)
            }
            catch (e: Exception) {
                _registerResult.value = RegisterResponse(message = e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }
}