package com.rrr.weatherapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rrr.weatherapp.data.Result
import com.rrr.weatherapp.data.WeatherDataManager
import com.rrr.weatherapp.data.WeatherDataModel
import com.rrr.weatherapp.data.local.LocalDataSource
import com.rrr.weatherapp.data.network.MetaWeatherApi
import com.rrr.weatherapp.data.network.model.LocationDetails
import com.rrr.weatherapp.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class WeatherListViewModelTest {

    private val locations: List<String> = arrayListOf("Gothenburg")
//        , "Stockholm", "Mountain View",
//        "London", "New York", "Berlin")

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
    private lateinit var weatherDataManager: WeatherDataManager
    @Mock
    private lateinit var metaWeatherApi: MetaWeatherApi
    @Mock
    private lateinit var localDataSource: LocalDataSource
    @Mock
    private lateinit var testObserver: Observer<Result<List<WeatherDataModel>>>

    private lateinit var mockViewModel: WeatherListViewModel

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
        mockViewModel = WeatherListViewModel(weatherDataManager)
    }

    @Test
    fun fetchWeatherForecastDetails() {
        coroutinesTestRule.runBlockingTest {
            val locationDetails = LocationDetails("Gothenburg", "City", 123L, "1231231")
            Mockito.`when`(metaWeatherApi.getLocationDetails("Gothenburg")).thenReturn(
                listOf(
                    locationDetails
                )
            )
            Mockito.`when`(weatherDataManager.getLocationWOID("Gothenburg")).thenReturn(123L)
            Mockito.`when`(weatherDataManager.getWeatherForecastDetails(123L)).thenReturn(null)

            mockViewModel.weatherDetailsForecastObservable.observeForever(testObserver)
            mockViewModel.fetchWeatherForecastDetails(locations)
            // Verify
            Mockito.verify(testObserver).onChanged(Result.Loading)
            mockViewModel.weatherDetailsForecastObservable.removeObserver(testObserver)
        }
    }
}
