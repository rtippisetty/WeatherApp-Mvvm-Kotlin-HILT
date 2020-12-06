package com.rrr.weatherapp.di

import android.content.Context
import com.rrr.weatherapp.data.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(@ApplicationContext appContext: Context) = LocalDataSource(appContext)
}