package com.rrr.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherDetails(
    @SerializedName("id")
    val id: Long,
    @SerializedName("weather_state_name")
    val weather_state_name: String,
    @SerializedName("weather_state_abbr")
    val weather_state_abbr: String,
    @SerializedName("wind_direction_compass")
    val wind_direction_compass: String,
    @SerializedName("created")
    val created_time_stamp: String,
    @SerializedName("applicable_date")
    val applicable_date_string: String,
    @SerializedName("min_temp")
    val min_temp: Float,
    @SerializedName("max_temp")
    val max_temp: Float,
    @SerializedName("the_temp")
    val the_temp: Float,
    @SerializedName("wind_speed")
    val wind_speed: Double,
    @SerializedName("wind_direction")
    val wind_direction: Double,
    @SerializedName("air_pressure")
    val air_pressure: Float,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("visibility")
    val visibility: Double,
    @SerializedName("predictability")
    val predictability: Int
)