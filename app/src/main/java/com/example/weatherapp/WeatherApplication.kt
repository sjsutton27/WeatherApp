package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.di.AppModule
import com.example.weatherapp.di.AppModuleImpl

class WeatherApplication: Application() {
    companion object{
        lateinit var appModule: AppModule
    }
    override fun onCreate(){
        super.onCreate()
        appModule = AppModuleImpl(appContext = this)
    }
}