package com.example.weatherapp.common.utils

import com.example.weatherapp.common.constants.Constants
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UtilityHelper @Inject constructor() {

    fun convertKelvinToCelsius(kelvin: Double?): String {
        return kelvin?.let {
            val celsius = it - 273.15
            "${roundOffDecimal(celsius)}°C"
        } ?: ""
    }
    fun convertKelvinToFahrenheit(kelvin: Double?): String {
        return kelvin?.let {
            val fahrenheit = (it - 273.15)*9/5+32
            "${roundOffDecimal(fahrenheit)}°F"
        } ?: ""
    }

    fun convertToKm(value: Int?): String {
        return value?.let {
            val km = it.toDouble() / 1000
            "${roundOffDecimal(km, 2)} km"
        } ?: ""
    }
    fun convertToMiles(value: Int?): String {
        return value?.let {
            val miles = (it.toDouble() * 0.621371)/ 1000
            "${roundOffDecimal(miles, 2)} miles"
        } ?: ""
    }

    fun calculateWind(value: Double?): String {
        return value?.let {
            val km = it * 3.6
            "${roundOffDecimal(km, 2)} km/h"
        } ?: ""
    }
    fun calculateWindMph(value: Double?): String {
        return value?.let {
            val km = it * 3.6 * 0.621371
            "${roundOffDecimal(km, 2)} mph"
        } ?: ""
    }

    fun convertUnixToTime(unix: Long?, format: String = Constants.HH_MM_A): String {
        return unix?.let {
            val date = Date(it * 1000L)
            val sdf = SimpleDateFormat(format, Locale.US)
            sdf.timeZone = TimeZone.getDefault()
            sdf.format(date)
        } ?: ""
    }

    private fun roundOffDecimal(value: Double?, decimalScale: Int = 1): Double? {
        return value?.let {
            value.toBigDecimal().setScale(decimalScale, RoundingMode.UP).toDouble()
        }
    }

}