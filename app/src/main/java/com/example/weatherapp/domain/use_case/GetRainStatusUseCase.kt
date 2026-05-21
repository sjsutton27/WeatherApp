package com.example.weatherapp.domain.use_case

import com.example.weatherapp.common.resource.Resource
import com.example.weatherapp.data.repository.WeatherRepository

class GetRainStatusUseCase(
    private val repository: WeatherRepository
){

    suspend operator fun invoke(
        lat: Float,
        lon: Float
    ): Resource<Boolean> {

        return repository.isRaining(lat, lon)
    }
}