package com.example.weatherapp.data.remote

import com.example.weatherapp.data.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") appid: String,
    ): WeatherResponse?

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
    ): WeatherResponse?
}