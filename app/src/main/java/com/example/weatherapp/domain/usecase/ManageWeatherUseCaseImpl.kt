package com.example.weatherapp.domain.usecase

import com.example.weatherapp.common.constants.Constants
import com.example.weatherapp.common.utils.NetworkResponse
import com.example.weatherapp.common.utils.UtilityHelper
import com.example.weatherapp.data.dto.WeatherResponse
import com.example.weatherapp.data.dto.Main
import com.example.weatherapp.data.dto.Sys
import com.example.weatherapp.data.dto.Weather
import com.example.weatherapp.domain.entity.*
import com.example.weatherapp.domain.repository.GetWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ManageWeatherUseCaseImpl @Inject constructor(
    private val getWeatherRepository: GetWeatherRepository,
    private val utilityHelper: UtilityHelper
) : ManageWeatherUseCase {

    override suspend fun getWeatherReport(lat: Double, lng: Double): Flow<NetworkResponse<CurrentWeather>> = flow {
        emit(NetworkResponse.Loading())
        try {
            val data = getWeatherRepository.getWeatherReport(lat, lng)
            if (data != null) {
                // convert response to serializable obj=
                val mapData = mapToCurrentWeather(data)
                emit(NetworkResponse.Success(mapData))
            } else {
                emit(NetworkResponse.Error(Constants.NO_CONNECTION_MESSAGE))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            if (e.code() == 401) {
                emit(NetworkResponse.Error(Constants.INCORRECT_API_KEY_MESSAGE))
            } else {
                emit(NetworkResponse.Error(e.localizedMessage ?: Constants.NO_CONNECTION_MESSAGE))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(NetworkResponse.Error(e.message ?: Constants.NO_CONNECTION_MESSAGE))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResponse.Error(Constants.NO_CONNECTION_MESSAGE))
        }
    }

    override suspend fun getWeatherReport(q: String): Flow<NetworkResponse<CurrentWeather>> = flow  {
        emit(NetworkResponse.Loading())
        try {
            val data = getWeatherRepository.getWeatherReport(q)
            if (data != null) {
                // convert response to serializable obj=
                val mapData = mapToCurrentWeather(data)
                emit(NetworkResponse.Success(mapData))
            } else {
                emit(NetworkResponse.Error(Constants.NO_CONNECTION_MESSAGE))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            if (e.code() == 401) {
                emit(NetworkResponse.Error(Constants.INCORRECT_API_KEY_MESSAGE))
            } else {
                emit(NetworkResponse.Error(e.localizedMessage ?: Constants.NO_CONNECTION_MESSAGE))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(NetworkResponse.Error(e.message ?: Constants.NO_CONNECTION_MESSAGE))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResponse.Error(Constants.NO_CONNECTION_MESSAGE))
        }
    }

    private fun mapToCurrentWeather(response: WeatherResponse): CurrentWeather {
        return CurrentWeather(
            temperature = mapToTemperature(response.main),
            description = mapToDescription(response.weather),
            visibility = utilityHelper.convertToMiles(response.visibility),
            windSpeed = utilityHelper.calculateWindMph(response.wind.speed),
            cloudPercent = "${response.clouds?.all}%",
            cityName = response.name,
            timeZone = response.timezone,
            rain = mapToRain(response.rain),
            sunRiseAndSet = mapToSunriseAndSunSet(response.sys),
            dateTimeUpdated = response.dateTimeUpdated
        )
    }

    private fun mapToSunriseAndSunSet(sys: Sys?): SunRiseAndSet {
        return SunRiseAndSet(
            sunRise = utilityHelper.convertUnixToTime(sys?.sunrise),
            sunSet = utilityHelper.convertUnixToTime(sys?.sunset),
            country = sys?.country ?: ""
        )
    }

    private fun mapToTemperature(main: Main?): Temperature {
        return Temperature(
            temp = utilityHelper.convertKelvinToFahrenheit(main?.temp),
            temp_feels_like = utilityHelper.convertKelvinToFahrenheit(main?.feels_like),
            min_temp = utilityHelper.convertKelvinToFahrenheit(main?.temp_min),
            max_temp = utilityHelper.convertKelvinToFahrenheit(main?.temp_max),
            humidity = "${main?.humidity}%"
        )
    }

    private fun mapToDescription(weather: List<Weather>?): Description? {
        weather?.let { it ->
            if (it.isNotEmpty()) {
                val firstItem = weather.first()
                return Description(
                    mainDesc = firstItem.description ?: "",
                    mainTitle = firstItem.main?.uppercase() ?: "",
                    weatherIcon = firstItem.let {
                        "${Constants.BASE_ICON_URL}/${it.icon}@2x.png"
                    }
                )
            }
        }
        return null
    }

    private fun mapToRain(rain: com.example.weatherapp.data.dto.Rain?): Rain {
        return Rain(rainInOneHr = rain?.rainInOneHr?.toString() ?: "")
    }
}