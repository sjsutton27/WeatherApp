package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.common.Constants.BASE_URL
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
    val weatherApi: WeatherApi
    val weatherRepository: WeatherRepository
}
