package com.example.weatherapp.domain.usecase

import com.example.weatherapp.common.utils.Response
import com.example.weatherapp.domain.entity.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface ManageWeatherUseCase {

    suspend fun getWeatherReport(
        lat: Double,
        lng: Double
    ): Flow<Response<CurrentWeather>>

    suspend fun getWeatherReport(
        q: String
    ): Flow<Response<CurrentWeather>>
}