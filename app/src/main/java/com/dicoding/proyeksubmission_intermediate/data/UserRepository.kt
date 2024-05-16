package com.dicoding.proyeksubmission_intermediate.data

import com.dicoding.proyeksubmission_intermediate.data.api.ApiService
import com.dicoding.proyeksubmission_intermediate.data.pref.UserModel
import com.dicoding.proyeksubmission_intermediate.data.pref.UserPreference
import com.dicoding.proyeksubmission_intermediate.data.response.DetailStoryResponse
import com.dicoding.proyeksubmission_intermediate.data.response.LoginResponse
import com.dicoding.proyeksubmission_intermediate.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getUserToken(): String {
        return getSession().first().token
    }

    suspend fun getDetailStory(storyId: String): DetailStoryResponse {
        return apiService.getDetailStory(storyId)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}