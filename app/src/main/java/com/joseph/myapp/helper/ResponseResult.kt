package com.joseph.myapp.helper

sealed class ResponseResult<out R> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val error: String) : ResponseResult<Nothing>()
    data class Failed<out T>(val reason: Enum<*>, val data: T) : ResponseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
            is Failed<*> -> "Failed[reason=$reason,data=$data]"
        }
    }
}