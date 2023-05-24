package com.example.weatherapp.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.ui.screens.AppSurface
import com.example.weatherapp.ui.screens.findActivity
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.text.input.TextFieldValue
import com.example.weatherapp.ui.screens.WeatherScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // Location permission state
                val multiplePermissionsState =
                    rememberMultiplePermissionsState(listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                AppSurface {
                    AskLocationPermission(multiplePermissionsState = multiplePermissionsState)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AskLocationPermission(multiplePermissionsState: MultiplePermissionsState) {
 //   val context = LocalContext.current

    LaunchedEffect(multiplePermissionsState) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    when {
        multiplePermissionsState.allPermissionsGranted -> {
            // Permission Granted
            MainScreen()
        }
        multiplePermissionsState.shouldShowRationale -> {
            LaunchedEffect(key1 = multiplePermissionsState) {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        }
        multiplePermissionsState.revokedPermissions.isNotEmpty() -> {
            // Denied some permission
           MainScreen()
        }
    }
}

@Composable
private fun MainScreen( viewModel: WeatherViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val text = remember { mutableStateOf(TextFieldValue(viewModel.getLastCity()?:"")) }
    BackHandler {
        context.findActivity()?.finish()
    }

    Row(verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp), horizontalArrangement = Arrangement.Start
    ) {OutlinedTextField(value = text.value,        onValueChange = { text.value = it },        label = { Text("City") })
        Button(onClick = {if(text.value.text.isNotEmpty())viewModel.getCurrentWeather(text.value.text)}, modifier = Modifier.height(55.dp)) { Text(text = "Change City") }
    }
    WeatherScreen()
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        AppSurface {
            MainScreen()
        }
    }
}