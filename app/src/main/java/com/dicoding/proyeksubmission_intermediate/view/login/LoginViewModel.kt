package com.dicoding.proyeksubmission_intermediate.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                _loginResult.postValue(result)
                result.loginResult?.token?.let { token ->
                    val user = UserModel(email = email, token = token, isLogin = true)
                    saveSession(user)
                }
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorMessage = errorResponse?.let {
                    Gson().fromJson(it, LoginResponse::class.java).message
                } ?: e.message()
                _loginResult.postValue(LoginResponse(message = errorMessage))
            } catch (e: Exception) {
                _loginResult.postValue(LoginResponse(message = e.message ?: "Unknown error"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}