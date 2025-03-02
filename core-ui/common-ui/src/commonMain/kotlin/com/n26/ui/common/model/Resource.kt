package com.n26.ui.common.model

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}
