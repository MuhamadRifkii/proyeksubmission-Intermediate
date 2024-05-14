package com.dicoding.proyeksubmission_intermediate.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.api.LoginResponse
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            val result = repository.login(email, password)
//            _loginResult.value = result
//        }
//    }

    suspend fun login(email: String, password: String): LoginResponse {
        _isLoading.value = true
        try {
            return repository.login(email, password)
        } finally {
            _isLoading.value = false
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}