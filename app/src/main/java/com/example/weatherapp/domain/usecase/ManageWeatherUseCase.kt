package com.example.weatherapp.domain.usecase

import com.example.weatherapp.common.utils.NetworkResponse
import com.example.weatherapp.domain.entity.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface ManageWeatherUseCase {

    suspend fun getWeatherReport(
        lat: Double,
        lng: Double
    ): Flow<NetworkResponse<CurrentWeather>>

    suspend fun getWeatherReport(
        q: String
    ): Flow<NetworkResponse<CurrentWeather>>
}