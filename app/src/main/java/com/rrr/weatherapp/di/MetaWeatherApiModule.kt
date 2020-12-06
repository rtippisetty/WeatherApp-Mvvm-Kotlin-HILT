package com.rrr.weatherapp.di

import com.rrr.weatherapp.BuildConfig
import com.rrr.weatherapp.data.Utils
import com.rrr.weatherapp.data.network.MetaWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object MetaWeatherApiModule {

    private const val TIME_OUT = 60L
    private const val MAX_REQUESTS = 10
    private val dispatcher = Dispatcher().apply { maxRequestsPerHost = MAX_REQUESTS }

    @Singleton
    @Provides
    fun getMetaWeatherApi(retrofit: Retrofit): MetaWeatherApi =
        retrofit.create(MetaWeatherApi::class.java)

    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun okhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(httpLoggingInterceptor)
        .dispatcher(dispatcher)
        .build()

    @Singleton
    @Provides
    fun httpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

}