package com.example.fitfluent.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    // Represents the success status with data.
    data class Success<out T : Any>(val data: T) : Result<T>()
    // Represents the error status with an exception.
    data class Error(val exception: Exception) : Result<Nothing>()

    // Returns a string representation of the Result object.
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}