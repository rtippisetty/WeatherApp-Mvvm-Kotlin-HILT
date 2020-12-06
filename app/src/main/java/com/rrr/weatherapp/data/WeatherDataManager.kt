package com.rrr.weatherapp.data

import android.util.Log
import com.rrr.weatherapp.data.local.LocalDataSource
import com.rrr.weatherapp.data.network.MetaWeatherApi
import com.rrr.weatherapp.data.network.model.WeatherForecastDetails
import javax.inject.Inject

/**
 * Weather data manager which handles weather forecast fetch operations
 */
open class WeatherDataManager @Inject constructor (
    private val metaWeatherApi: MetaWeatherApi,
    private val localDataSource: LocalDataSource
) {
    private val TAG = WeatherDataManager::class.java.simpleName

    /**
     * Get location WOID for [locationName]
     */
    suspend fun getLocationWOID(locationName: String): Long {
        Log.d(TAG, "getLocationWOID for location: $locationName")
        // Value not in local store
        if(!localDataSource.hasLocationWOID(locationName)) {
            Log.d(TAG, "API invoked to get WOID for location: $locationName")
            val woid = metaWeatherApi.getLocationDetails(locationName)[0].woeid
            // Update local store
            Log.d(TAG, "$locationName woid = $woid")
            localDataSource.setLocationWOID(locationName, woid)
        }
        // Return local store value
        return localDataSource.getLocationWOID(locationName)
    }

    /**
     * Get Weather forecast details for [woid]
     */
    suspend fun getWeatherForecastDetails(woid: Long): WeatherForecastDetails {
        val weatherForecastDetails = metaWeatherApi.getConsolidateWeatherData(woid)
        Log.d(TAG, "Forecast details for $woid = $weatherForecastDetails")
        return weatherForecastDetails
    }
}