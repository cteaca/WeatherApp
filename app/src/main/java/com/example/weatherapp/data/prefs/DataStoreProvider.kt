package com.example.weatherapp.data.prefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * The SharedPreferences should be replaced with DataStore because this runs on main thread without suspend
 */
class DataStoreProvider @Inject constructor(@ApplicationContext val appContext: Context) {

    private val appPreferences = appContext.getSharedPreferences(PREF_FILE_APP, Context.MODE_PRIVATE)

    var lastCity: String?
        get() {
            return appPreferences.getString(PREF_KEY_LAST_CITY, null)
        }
        set(value) {
            appPreferences.edit {
                putString(PREF_KEY_LAST_CITY, value).apply()
            }
        }

    companion object {
        const val PREF_FILE_APP = "prefApp"
        const val PREF_KEY_LAST_CITY = "pref_city"
    }
}