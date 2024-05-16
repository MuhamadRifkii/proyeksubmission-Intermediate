package com.dicoding.proyeksubmission_intermediate.data

sealed class FetchResult<out T> {
    data class Success<out T>(val data: T) : FetchResult<T>()
    data class Error(val exception: Throwable) : FetchResult<Nothing>()
    object Loading : FetchResult<Nothing>()
}