package com.rrr.weatherapp.data

import android.text.format.DateUtils
import java.text.SimpleDateFormat

object Utils {
    /**
     * Meta weather base url
     */
    const val BASE_URL = "https://www.metaweather.com"

    /**
     * Helper method to check the give date is of tomorrow's
     */
    fun isTomorrow(dateString: String): Boolean {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateString)
        return DateUtils.isToday(date.time - DateUtils.DAY_IN_MILLIS)
    }
}