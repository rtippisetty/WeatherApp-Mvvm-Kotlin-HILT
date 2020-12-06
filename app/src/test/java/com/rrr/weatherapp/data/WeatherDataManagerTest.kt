package com.rrr.weatherapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rrr.weatherapp.data.local.LocalDataSource
import com.rrr.weatherapp.data.network.MetaWeatherApi
import com.rrr.weatherapp.data.network.model.LocationDetails
import com.rrr.weatherapp.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherDataManagerTest {
    private val woidLondon = 44418L

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule: TestCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var sharedPreferences: SharedPreferences
    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var metaWeatherApi: MetaWeatherApi

    @Mock
    private lateinit var localDataSource: LocalDataSource

    private lateinit var weatherDataManager: WeatherDataManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`<Any>(
            context.getSharedPreferences(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(sharedPreferences)
        Mockito.`when`<Any>(
            context.getSharedPreferences(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            ).edit()
        ).thenReturn(mockEditor)
        localDataSource = LocalDataSource(context)
        weatherDataManager = WeatherDataManager(metaWeatherApi, localDataSource)
    }

    @Test
    fun getLocationWOIDWithLocalStore() {
       coroutinesTestRule.runBlockingTest {
           Mockito.`when`(localDataSource.hasLocationWOID("London"))
               .thenReturn(true)
           Mockito.`when`(localDataSource.getLocationWOID("London"))
               .thenReturn(woidLondon)
           assertEquals(woidLondon, weatherDataManager.getLocationWOID("London"))
       }
    }

    @Test
    fun getLocationWOIDWithoutLocalStore() {
        coroutinesTestRule.runBlockingTest {
            Mockito.`when`(localDataSource.hasLocationWOID("London"))
                .thenReturn(false)
            Mockito.`when`(localDataSource.getLocationWOID("London"))
                .thenReturn(woidLondon)
            Mockito.`when`(localDataSource.setLocationWOID(
                "London",
                woidLondon)).then {  }
            val locationDetails = LocationDetails("London", "City", woidLondon, "1231231")
            Mockito.`when`(metaWeatherApi.getLocationDetails("London")).thenReturn(
                listOf(
                    locationDetails
                )
            )
            val result = weatherDataManager.getLocationWOID("London")
            assertEquals(woidLondon, result)
        }
    }

    @Test
    fun getWeatherForecastDetails() {
        coroutinesTestRule.runBlockingTest {
            Mockito.`when`(metaWeatherApi.getConsolidateWeatherData(woidLondon)) .thenReturn(
                null
            )
            val result = weatherDataManager.getWeatherForecastDetails(woidLondon)
            assertEquals(null, result)
        }
    }
}