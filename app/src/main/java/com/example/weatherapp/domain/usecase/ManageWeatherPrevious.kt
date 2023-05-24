package com.example.weatherapp.domain.usecase

interface ManageWeatherPrevious {

     fun saveLastCity(city: String?)

     fun getLastCity(): String?
}