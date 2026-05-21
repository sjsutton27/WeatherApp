package com.example.weatherapp.presentation.states

import com.example.weatherapp.data.responses.Coord

data class CoordinatesState(
    val coordinates: Coord = Coord(
        lat = 0f,
        lon = 0f
    ),

    val isRaining: Boolean? = null,

    val error: Int? = null,

    val isLoading: Boolean = false,
)