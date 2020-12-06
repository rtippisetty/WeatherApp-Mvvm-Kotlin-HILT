package com.rrr.weatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalDataSourceTest {
    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        Mockito.`when`<Any>(
            context.getSharedPreferences(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt()
            )
        ).thenReturn(sharedPreferences)

        localDataSource = LocalDataSource(context)

    }

    @Test
    fun hasLocationWOIDTestFalse() {
        Mockito.`when`<Any>(sharedPreferences.contains(
            ArgumentMatchers.anyString()
        )).thenReturn(false)

        assertFalse(localDataSource.hasLocationWOID("Stockholm"))
    }

    @Test
    fun hasLocationWOIDTestTrue() {
        Mockito.`when`<Any>(sharedPreferences.contains(
            ArgumentMatchers.anyString()
        )).thenReturn(true)

        assertTrue(localDataSource.hasLocationWOID("Stockholm"))
    }

    @Test
    fun getLocationWOID() {
        Mockito.`when`<Any>(sharedPreferences.getLong(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.anyLong()
        )).thenReturn(123L)

        assertEquals(123L, localDataSource.getLocationWOID("Stockholm"))
    }
}