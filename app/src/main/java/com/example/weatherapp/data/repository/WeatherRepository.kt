package com.example.weatherapp.data.repository

import com.example.weatherapp.common.Constants.API_KEY
import com.example.weatherapp.common.resource.Resource
import com.example.weatherapp.data.api.WeatherApi

class WeatherRepository(
    private val api: WeatherApi
){

    suspend fun isRaining(
        lat: Float,
        lon: Float
    ): Resource<Boolean> {

        val response = try{

            api.getCoordinatesInfo(
                lat = lat,
                lon = lon,
                apiKey = API_KEY
            )

        }catch(e: Exception){

            return Resource.Error(
                message = "Unknown Error Occurred"
            )
        }

        val raining =
            response.weather.any {weather ->
                weather.main == "Rain"
            }

        return Resource.Success(
            data = raining
        )
    }
}