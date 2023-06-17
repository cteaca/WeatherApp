package com.example.weatherapp.data.repository

import com.example.weatherapp.common.constants.Constants
import com.example.weatherapp.data.dto.WeatherResponse
import com.example.weatherapp.data.remote.WeatherApi
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetWeatherInfoImplTest {

    private lateinit var getWeatherInfoImpl: GetWeatherRepositoryImpl
    private lateinit var weatherApi: WeatherApi

    @Before
    fun setUp() {
        mockkStatic(Constants::class)
        weatherApi = mockk(relaxed = true)
        getWeatherInfoImpl = GetWeatherRepositoryImpl(
            weatherApi
        )
    }

    @After
    fun tearDown(){
        unmockkAll()
    }

    @Test
    fun `weather report should equal to expected response`() = runBlocking {
        val mockResponse = mockk<WeatherResponse> {
            every {
                base
            } returns "Hello base"
        }
        coEvery {
            weatherApi.getCurrentWeather(any(), any(), any())
        } returns mockResponse

        val expectedRun = getWeatherInfoImpl.getWeatherReport(0.0, 0.0)
        assert(expectedRun == mockResponse)
    }
}