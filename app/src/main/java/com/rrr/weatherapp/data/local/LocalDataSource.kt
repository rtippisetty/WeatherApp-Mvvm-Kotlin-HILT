package com.rrr.weatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Class to hold local store values
 */
open class LocalDataSource @Inject constructor(context: Context) {
    companion object {
        private const val NAME = "WeatherAppPreferences"
        private const val MODE = Context.MODE_PRIVATE
        private const val DEF_VALUE: Long = -1L
        const val LOCATION = "LOCATION"
    }
    private var preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit() // create an Editor
        operation(editor)
        editor.apply() // write to disk asynchronously
    }

    /**
     * check for [location]'s woid value in local store
     */
    fun hasLocationWOID(location: String): Boolean = preferences.contains(LOCATION + location)

    /**
     * returns woid value for [location]
     */
    fun getLocationWOID(location: String): Long = preferences.getLong(LOCATION + location, DEF_VALUE)

    /**
     * store [woid] of [location] in locally
     */
    fun setLocationWOID(location: String, woid: Long) {
        preferences.edit {
            it.putLong(LOCATION + location, woid)
            it.apply()
        }
    }
}