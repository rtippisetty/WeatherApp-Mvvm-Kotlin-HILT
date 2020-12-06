package com.rrr.weatherapp.data

import com.rrr.weatherapp.data.network.model.LocationDetails
import com.rrr.weatherapp.data.network.model.WeatherDetails

/**
 * UI Data model for display
 */
data class WeatherDataModel(
    val locationModel: LocationDetails,
    val weatherDetails: WeatherDetails
) {
    override fun equals(other: Any?): Boolean {
        return (other as WeatherDataModel).locationModel == this.locationModel
    }
}