package com.example.weatherapp.data.api

import com.example.weatherapp.data.responses.Data
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCoordinatesInfo(

        @Query("lat")
        lat: Float,

        @Query("lon")
        lon: Float,

        @Query("appid")
        apiKey: String

    ): Data
}