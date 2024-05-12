package com.dicoding.proyeksubmission_intermediate.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(name, email, password)
        }
    }
}