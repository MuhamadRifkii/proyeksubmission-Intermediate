package com.dicoding.proyeksubmission_intermediate.data

import com.dicoding.proyeksubmission_intermediate.data.api.ApiConfig
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    private val apiService = ApiConfig.getApiService()

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun registerUser(name: String, email: String, password: String) {
        apiService.register(name, email, password)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}