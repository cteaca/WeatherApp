package com.example.weatherapp.domain.repository

interface HistoryRepository {

    fun getLastCity(): String?

    fun setLastCity(
        city: String?
    )
}