package com.dicoding.proyeksubmission_intermediate.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.api.RegisterResponse

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    fun register(name: String, email: String, password: String) {
//        viewModelScope.launch {
//            val result = repository.register(name, email, password)
//            _registerResult.value = result
//        }
//    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        _isLoading.value = true
        try {
            return repository.register(name, email, password)
        } finally {
            _isLoading.value = false
        }
    }
}