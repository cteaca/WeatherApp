package com.example.weatherapp.ui.uistate

import com.example.weatherapp.data.entity.CurrentWeather

sealed class WeatherState {
    data class OnDisplayData(val data: CurrentWeather?) : WeatherState()
    data class OnError(val message: String? = "") : WeatherState()
    object OnLoading : WeatherState()
}
