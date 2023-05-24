package com.example.weatherapp.data.usecase

import android.app.Application
import android.content.Context
import com.example.weatherapp.domain.usecase.ManageWeatherPrevious
import javax.inject.Inject

class ManageWeatherPreviousImpl @Inject constructor(private val context: Application) : ManageWeatherPrevious {

    override  fun saveLastCity(city: String?) {
        val sharedPref = context.getSharedPreferences("prefApp", Context.MODE_PRIVATE)
        sharedPref.edit().putString("pref_city", city).apply()
    }

    override  fun getLastCity(): String? {
        val sharedPref = context.getSharedPreferences("prefApp", Context.MODE_PRIVATE)
        return sharedPref.getString("pref_city", null)
    }

}