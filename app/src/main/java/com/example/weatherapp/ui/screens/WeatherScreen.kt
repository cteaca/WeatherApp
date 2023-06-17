package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weatherapp.domain.entity.CurrentWeather
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.uistate.WeatherState
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    when (val result = uiState) {
        is WeatherState.OnDisplayData -> {
            ShowWeatherScreen(result.data)
        }
        is WeatherState.OnError -> {
            // Show error
            ShowErrorMessage(text = result.message, actionMessage = "Refresh") {
                viewModel.getCurrentLocation()
            }
        }
        is WeatherState.OnLoading -> {
            ShowLoading()
        }
        else -> {
//            ShowErrorMessage(text = result.message)
        }
    }
}

@Composable
private fun ShowWeatherScreen(result: CurrentWeather?) {
    Spacer(modifier = Modifier.height(30.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), horizontalArrangement = Arrangement.Center) {
            val locationName = "${result?.cityName}, ${result?.sunRiseAndSet?.country}"
            Text(text = locationName, style = MaterialTheme.typography.h4)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp), horizontalArrangement = Arrangement.Start
        ) {
            Column(modifier = Modifier
                .height(150.dp)
                .weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = result?.temperature?.temp ?: "", style = MaterialTheme.typography.h3)
                val tempFeelsLike = "Feels like ${result?.temperature?.temp_feels_like}"
                Text(text = tempFeelsLike, style = MaterialTheme.typography.body1)
            }

            Column(modifier = Modifier
                .height(130.dp)
                .width(150.dp)) {
                result?.description?.let {
                    println("weather icon: ${it.weatherIcon}")
                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .weight(0.4f), model = it.weatherIcon, contentDescription = null
                    )
                }
                val desc = result?.description.toString()
                Text(text = desc, style = MaterialTheme.typography.h6)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        val highLow = "Low ${result?.temperature?.min_temp ?: "--"} / High ${result?.temperature?.max_temp ?: "--"}"
        Text(text = highLow, style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Humidity: ${result?.temperature?.humidity ?: ""}", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = "Sunrise: ${result?.sunRiseAndSet?.sunRise ?: ""}", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Sunset: ${result?.sunRiseAndSet?.sunSet ?: ""}", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = "Cloudiness: ${result?.cloudPercent ?: ""}", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Visibility: ${result?.visibility ?: ""}", style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = "Wind: ${result?.windSpeed ?: ""}", style = MaterialTheme.typography.subtitle1)


        result?.rain?.let {
            if (it.rainInOneHr?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Rain: ${it.rainInOneHr}", style = MaterialTheme.typography.subtitle1)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrentWeatherScreen() {
    WeatherAppTheme {
        WeatherScreen()
    }
}