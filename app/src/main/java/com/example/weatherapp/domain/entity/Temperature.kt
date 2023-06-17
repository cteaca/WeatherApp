package com.example.weatherapp.domain.entity

data class Temperature(
    val temp: String,
    val temp_feels_like: String,
    val min_temp: String,
    val max_temp: String,
    val humidity: String
)