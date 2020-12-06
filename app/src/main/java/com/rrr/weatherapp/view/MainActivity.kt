package com.rrr.weatherapp.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.rrr.weatherapp.R
import com.rrr.weatherapp.data.Result
import com.rrr.weatherapp.data.WeatherDataModel
import com.rrr.weatherapp.databinding.ActivityMainBinding
import com.rrr.weatherapp.viewmodel.WeatherListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val locations: List<String> = arrayListOf("Gothenburg", "Stockholm", "Mountain View",
        "London", "New York", "Berlin")

    private val mainViewModel: WeatherListViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherListAdapter: WeatherListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize the ui components
        supportActionBar?.title = getString(R.string.app_title)
        setupSwipeRefresh()
        setupWeatherForecastObserver()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            Toast.makeText(this, "Refresh in progress..", Toast.LENGTH_SHORT).show()
            refreshData()
        }
    }

    private fun setupRecyclerView() = with(binding.weatherListRecycler){
        weatherListAdapter = WeatherListAdapter(mutableListOf())
        adapter =weatherListAdapter
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            val drawable = ContextCompat.getDrawable(context, R.drawable.divider_decorator)
            drawable?.run { setDrawable(this) }
        })
    }

    /**
     * setup weather forecast updates observer to receive recent data from a stream
     */
    private fun setupWeatherForecastObserver() {
        mainViewModel.weatherDetailsForecastObservable.observe(this, { result ->
            when(result) {
                Result.Loading -> showProgress()
                is Result.Error -> {
                    hideProgress()
                    displayError(result)
                }
                is Result.Success -> {
                    hideProgress()
                    Log.d(TAG, result.toString())
                    updateWeatherAdapter(result.data)
                }
            }
        })
    }

    private fun refreshData() {
        mainViewModel.fetchWeatherForecastDetails(locations)
    }

    private fun showProgress() {
        binding.swipeRefresh.isRefreshing = true
    }

    private fun hideProgress() {
        binding.swipeRefresh.isRefreshing = false
    }

    private fun displayError(message: Result.Error) {
        Log.d(TAG, message.toString())
        Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
    }

    /**
     * Helper method to update weather adapter
     */
    private fun updateWeatherAdapter(weatherList: List<WeatherDataModel>) {
        weatherListAdapter.updateList(weatherList)
    }
}