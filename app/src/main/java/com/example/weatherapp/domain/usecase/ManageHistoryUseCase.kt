package com.example.weatherapp.domain.usecase

interface ManageHistoryUseCase {

     fun saveLastCity(city: String?)

     fun getLastCity(): String?
}