package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.dto.WeatherResponse

interface GetWeatherRepository {

    suspend fun getWeatherReport(
        lat: Double,
        lng: Double
    ): WeatherResponse?

    suspend fun getWeatherReport(
        q: String
    ): WeatherResponse?
}