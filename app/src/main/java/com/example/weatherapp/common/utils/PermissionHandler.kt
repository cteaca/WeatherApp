package com.example.weatherapp.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.weatherapp.common.constants.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PermissionHandler @Inject constructor(@ApplicationContext private val context: Context) {

   @SuppressLint("MissingPermission")//"Permission check is already in isPermissionGranted() method"
    fun getCurrentLocation(): Flow<Response<Location>> = callbackFlow {
        trySend(Response.Loading())
        if (isPermissionGranted()) {
            try {
                val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                    .addOnSuccessListener {
                        trySend(Response.Success(it))
                    }.addOnFailureListener {
                        trySend(Response.Error(Constants.FAILED_FETCH_USER_LOCATION))
                    }
                awaitClose { cancel() }
            } catch (e: Exception) {
                trySend(Response.Error(Constants.FAILED_FETCH_USER_LOCATION))
            }
        } else {
            trySend(Response.Error(Constants.FAILED_FETCH_USER_LOCATION))
        }
    }

     fun isPermissionGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
}