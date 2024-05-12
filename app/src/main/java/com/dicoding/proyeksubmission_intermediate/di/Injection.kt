package com.dicoding.proyeksubmission_intermediate.di

import android.content.Context
import com.dicoding.proyeksubmission_intermediate.data.UserRepository
import com.dicoding.proyeksubmission_intermediate.data.pref.UserPreference
import com.dicoding.proyeksubmission_intermediate.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}