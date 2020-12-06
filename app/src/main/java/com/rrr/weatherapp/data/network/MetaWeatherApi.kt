package com.rrr.weatherapp.data.network

import com.rrr.weatherapp.data.network.model.LocationDetails
import com.rrr.weatherapp.data.network.model.WeatherForecastDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetaWeatherApi {
    // Location Search - Find a location
    @GET("/api/location/search/")
    suspend fun getLocationDetails(@Query("query") query: String): List<LocationDetails>

    // Location - Location information, and a 5 day forecast
    @GET("/api/location/{woeid}/")
    suspend fun getConsolidateWeatherData(@Path("woeid") woeid: Long): WeatherForecastDetails
}