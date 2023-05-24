package com.example.weatherapp

import com.example.weatherapp.common.utils.UtilityHelper
import org.junit.Assert
import org.junit.Test

class UtilityUnitTest {
    private var utilityHelper = UtilityHelper()

    @Test
    fun convertKelvinToCelsiusTest() {
        Assert.assertEquals("26.9°C", utilityHelper.convertKelvinToCelsius(300.0))
        Assert.assertEquals("-173.2°C", utilityHelper.convertKelvinToCelsius(100.0))
        Assert.assertEquals("56.9°C", utilityHelper.convertKelvinToCelsius(330.0))
    }

    @Test
    fun convertKelvinToFahrenheitTest() {
        Assert.assertEquals("80.4°F", utilityHelper.convertKelvinToFahrenheit(300.0))
        Assert.assertEquals("-99.7°F", utilityHelper.convertKelvinToFahrenheit(200.0))
        Assert.assertEquals("134.4°F", utilityHelper.convertKelvinToFahrenheit(330.0))
    }

    @Test
    fun convertToKmTest() {
        Assert.assertEquals("0.3 km", utilityHelper.convertToKm(300))
    }

    @Test
    fun convertToMilesTest() {
        Assert.assertEquals("0.19 miles", utilityHelper.convertToMiles(300))
    }

    @Test
    fun calculateWindTest() {
        Assert.assertEquals("1080.0 km/h", utilityHelper.calculateWind(300.0))
    }

    @Test
    fun calculateWindMphTest() {
        Assert.assertEquals("671.09 mph", utilityHelper.calculateWindMph(300.0))
    }
}