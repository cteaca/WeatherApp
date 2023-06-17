package com.example.weatherapp.data.repository

import com.example.weatherapp.data.prefs.DataStoreProvider
import com.example.weatherapp.domain.repository.HistoryRepository
import javax.inject.Inject

/**
 * This can be made to use:
 * - SharedPreferences or DataStore
 * - Database using Room
 */
class HistoryRepositoryImpl @Inject constructor(private val dataStore: DataStoreProvider) : HistoryRepository {
    override fun getLastCity(): String? {
        return dataStore.lastCity
    }

    override fun setLastCity(city: String?) {
        dataStore.lastCity=city
    }
}