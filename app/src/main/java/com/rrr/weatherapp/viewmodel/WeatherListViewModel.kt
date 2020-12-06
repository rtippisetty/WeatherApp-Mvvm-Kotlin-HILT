package com.rrr.weatherapp.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrr.weatherapp.data.Result
import com.rrr.weatherapp.data.Utils
import com.rrr.weatherapp.data.WeatherDataManager
import com.rrr.weatherapp.data.WeatherDataModel
import com.rrr.weatherapp.data.network.model.LocationDetails
import com.rrr.weatherapp.data.network.model.WeatherForecastDetails
import kotlinx.coroutines.*

class WeatherListViewModel @ViewModelInject constructor(private val weatherDataManger: WeatherDataManager): ViewModel() {
    private val TAG = WeatherListViewModel::class.java.simpleName
    private val weatherListLiveData = MutableLiveData<Result<List<WeatherDataModel>>>()
    private val weatherList = mutableListOf<WeatherDataModel>()

    /**
     * Output stream to emit the list of Weather forecast details for provided locations
     */
    val weatherDetailsForecastObservable: LiveData<Result<List<WeatherDataModel>>>
        get() = weatherListLiveData

    /**
     * Fetch Weather forecast details for the list of [locations]
     */
    fun fetchWeatherForecastDetails(locations: List<String>){
        weatherListLiveData.postValue(Result.Loading)
        viewModelScope.launch {
            supervisorScope {
                try {
                    val resultList = locations.map { locationName ->
                        async {
                            // Fetch woid for location
                            val woid = weatherDataManger.getLocationWOID(locationName)
                            // Fetch weather forecast details for woid
                            val weatherForecastDetails = weatherDataManger.getWeatherForecastDetails(woid)
                            // Parse and get tomorrow's weather data
                            val locationWeather = getTomorrowWeatherData(weatherForecastDetails)
                            updateData(locationWeather)
                        }
                    }.awaitAll()
                    Log.d(TAG, "Weather forecast details : [$resultList]")
                    //weatherForecastList.postValue(Result.Success(resultList))
                } catch (exception: Exception) {
                    weatherListLiveData.postValue(Result.Error(exception))
                } finally {
                    this.cancel()
                }
            }
        }
    }

    /**
     * Helper method to filter tomorrow's weather forecast from [weatherForecastDetails] object
     */
    private fun getTomorrowWeatherData(weatherForecastDetails: WeatherForecastDetails): WeatherDataModel {
        val locationDetails = LocationDetails(
            weatherForecastDetails.title,
            weatherForecastDetails.location_type,
            weatherForecastDetails.woeid,
            weatherForecastDetails.latt_long
        )
        val weatherDetails = weatherForecastDetails.consolidated_weatherDetails.first{
            Utils.isTomorrow(it.applicable_date_string)
        }
        return WeatherDataModel(locationDetails, weatherDetails)
    }

    /**
     * Helper method to update the output stream with obtained [locationWeather]
     */
    private fun updateData(locationWeather: WeatherDataModel) {
        if(weatherList.contains(locationWeather)){
            weatherList.remove(locationWeather)
        }
        weatherList.add(locationWeather)
        weatherList.sortBy { it.locationModel.title }
        weatherListLiveData.postValue(Result.Success(weatherList))
    }
}