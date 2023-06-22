package com.example.weatherapp.common.utils

sealed class Response<T>(val data: T? = null, val message: String = "", val statusCode: Int? = null) {

    class Success<T>(data: T) : Response<T>(data)

    class Error<T>(message: String, data: T? = null) : Response<T>(message = message, data = data)

    class Loading<T> : Response<T>()
}