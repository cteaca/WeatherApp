package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.dto.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.domain.repository.GetWeatherRepository
import javax.inject.Inject

class GetWeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi) : GetWeatherRepository {

    override suspend fun getWeatherReport(lat: Double, lng: Double): WeatherResponse? {
        val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
        return weatherApi.getCurrentWeather(lat = lat, lng = lng, appid = apiKey)
    }

    override suspend fun getWeatherReport(q: String): WeatherResponse? {
        val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
        return weatherApi.getCurrentWeather( q, appid = apiKey)
    }
}