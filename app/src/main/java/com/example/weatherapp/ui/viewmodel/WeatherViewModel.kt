package com.example.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.common.utils.NetworkResponse
import com.example.weatherapp.common.utils.PermissionHandler
import com.example.weatherapp.domain.usecase.ManageHistoryUseCase
import com.example.weatherapp.domain.usecase.ManageWeatherUseCase
import com.example.weatherapp.ui.uistate.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val manageWeatherUseCase: ManageWeatherUseCase,
    private val permissionHandler: PermissionHandler,
    private val previousWeatherUseCase: ManageHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherState?>(null)
    val uiState: StateFlow<WeatherState?> get() = _uiState

    init {
        getCurrentLocation()
    }

    fun getLastCity():String?{
        return previousWeatherUseCase.getLastCity()
    }
    fun getCurrentLocation() {
        val lastCity = getLastCity()
        viewModelScope.launch {
            if (!lastCity.isNullOrEmpty()) {
                // Use the last city
                _uiState.value = WeatherState.OnLoading
                getCurrentWeather(lastCity)
            } else {
                // Try to use the device location
                if (permissionHandler.isPermissionGranted()) {
                    _uiState.value = WeatherState.OnLoading
                    permissionHandler.getCurrentLocation().collect { result ->
                        if (result.data != null) {
                            when (result) {
                                is NetworkResponse.Error -> {
                                    _uiState.value = WeatherState.OnError(result.message)
                                }
                                is NetworkResponse.Success -> {
                                    getCurrentWeather(result.data.latitude, result.data.longitude)
                                }
                                else -> _uiState.value = WeatherState.OnLoading
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * The weather by lat/long is only used when the app is started for the first time
     * and the device location permissions are granted.
     */
    private fun getCurrentWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            manageWeatherUseCase.getWeatherReport(lat, lng).collectLatest {
                when (val response = it) {
                    is NetworkResponse.Success -> {
                        previousWeatherUseCase.saveLastCity(response.data?.cityName)
                        _uiState.value = WeatherState.OnDisplayData(response.data)
                    }
                    is NetworkResponse.Error -> {
                        _uiState.value = WeatherState.OnError(response.message)
                    }
                    else -> {
                        _uiState.value = WeatherState.OnLoading
                    }
                }
            }
        }
    }

    /**
     * Note: Getting the weather using only the city name is not reliable because there can be multiple different cities with the same name.
     * The proper way to get the correct weather for the city is to either be more specific
     * or, preferably, use a two-step process and allow the user to select from multiple cities.
     * For this coding challenge, I kept it simple and use only the city.
     */
    fun getCurrentWeather(q: String) {
        viewModelScope.launch {
            manageWeatherUseCase
                .getWeatherReport(q)
                .collectLatest {
                when (val response = it) {
                    is NetworkResponse.Success -> {
                        previousWeatherUseCase.saveLastCity(response.data?.cityName)
                        _uiState.value = WeatherState.OnDisplayData(response.data)
                    }
                    is NetworkResponse.Error -> {
                        _uiState.value = WeatherState.OnError(response.message)
                    }
                    else -> {
                        _uiState.value = WeatherState.OnLoading
                    }
                }
            }
        }
    }
}