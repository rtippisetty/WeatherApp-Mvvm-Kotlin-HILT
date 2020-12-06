package com.rrr.weatherapp.data.network.model

import com.google.gson.annotations.SerializedName

data class WeatherForecastDetails(
    @SerializedName("consolidated_weather")
    val consolidated_weatherDetails: List<WeatherDetails>,
    @SerializedName("time")
    val timestamp: String,
    @SerializedName("sun_rise")
    val sun_rise_timestamp: String,
    @SerializedName("sun_set")
    val sun_set_timestamp: String,
    @SerializedName("timezone_name")
    val timezone_name: String,
    @SerializedName("parent")
    val parent: Parent,
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("title")
    val title: String,
    @SerializedName("location_type")
    val location_type: String,
    @SerializedName("woeid")
    val woeid: Long,
    @SerializedName("latt_long")
    val latt_long: String,
    @SerializedName("timezone")
    val timezone: String
) {

    inner class Parent(
        @SerializedName("title")
        val title: String,
        @SerializedName("location_type")
        val location_type: String,
        @SerializedName("woeid")
        val woeid: Long,
        @SerializedName("latt_long")
        val latt_long: String
    )

    inner class Source(
        @SerializedName("title")
        val title: String,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("crawl_rate")
        val crawl_rate: Int
    )
}