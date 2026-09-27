package com.lifeos.core.common

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null, val message: String? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

fun <T> Result<T>.dataOrNull(): T? = (this as? Result.Success)?.data

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> Result.Error(exception, message)
    is Result.Loading -> Result.Loading
}
