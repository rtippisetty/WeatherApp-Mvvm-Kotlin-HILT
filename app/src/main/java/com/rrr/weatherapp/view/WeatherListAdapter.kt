package com.rrr.weatherapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rrr.weatherapp.R
import com.rrr.weatherapp.data.Utils
import com.rrr.weatherapp.data.WeatherDataModel
import com.rrr.weatherapp.databinding.WeatherViewBinding
import com.squareup.picasso.Picasso

class WeatherListAdapter(private var weatherDataList: MutableList<WeatherDataModel>):
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WeatherViewBinding.inflate(layoutInflater)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherDataList[position])
    }

    override fun getItemCount() = weatherDataList.size

    fun updateList(newList: List<WeatherDataModel>) {
        weatherDataList.clear()
        weatherDataList.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * Weather forecast details view
     */
    inner class WeatherViewHolder(private val binding: WeatherViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherDataModel: WeatherDataModel) = with(binding){
            title.text = weatherDataModel.locationModel.title
            weatherState.text = weatherDataModel.weatherDetails.weather_state_name
            tempMax.text = itemView.context.getString(R.string.max_label, "%.2f".format(weatherDataModel.weatherDetails.max_temp))
            tempMin.text = itemView.context.getString(R.string.min_label, "%.2f".format(weatherDataModel.weatherDetails.min_temp))
            humidity.text = itemView.context.getString(R.string.humidity_label, weatherDataModel.weatherDetails.humidity)
            visibility.text = itemView.context.getString(R.string.visibility_label,"%.1f".format(weatherDataModel.weatherDetails.visibility))
            confidence.text = itemView.context.getString(R.string.confidence_label, weatherDataModel.weatherDetails.predictability)
            windSpead.text = itemView.context.getString(R.string.wind_label, "%.1f".format(weatherDataModel.weatherDetails.wind_speed))
            dayLabel.text = itemView.context.getString(R.string.tomorrow_label, weatherDataModel.weatherDetails.applicable_date_string)
            loadIcon(icon, weatherDataModel.weatherDetails.weather_state_abbr)
        }

        private fun loadIcon(targetView: ImageView, imageCode: String) {
            val imageUrl = Utils.BASE_URL + "/static/img/weather/png/64/${imageCode}.png"
            val picasso = Picasso.get()
            picasso.isLoggingEnabled = true
            picasso
                .load(imageUrl)
                .error(R.drawable.cloud_off)
                .placeholder(R.drawable.cloud_download)
                .into(targetView)
        }
    }

}