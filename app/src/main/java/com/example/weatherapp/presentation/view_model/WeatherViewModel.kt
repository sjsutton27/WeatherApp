package com.example.weatherapp.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.common.resource.Resource
import com.example.weatherapp.data.responses.Coord
import com.example.weatherapp.domain.use_case.GetRainStatusUseCase
import com.example.weatherapp.presentation.states.CoordinatesState
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val useCase: GetRainStatusUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CoordinatesState())
    val state: State<CoordinatesState> = _state
    private val _navigateToRain = mutableStateOf<Coord?>(null)
    val navigateToRain: State<Coord?> = _navigateToRain

    init {
        clearCoordinates()
    }

    fun checkRainStatus(lat: Float, lon: Float) {

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {

            when (val result = useCase(lat, lon)) {

                is Resource.Success -> {

                    val coord = Coord(lat = lat, lon = lon)

                    _state.value = _state.value.copy(
                        coordinates = coord,
                        isRaining = result.data,
                        isLoading = false,
                        error = null
                    )

                    _navigateToRain.value = coord
                }

                is Resource.Error -> {

                    _state.value = _state.value.copy(
                        isRaining = false,
                        isLoading = false,
                        error = R.string.generic_error
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun onCoordinatesEntered(input: String) {

        _state.value = _state.value.copy(error = null)

        val (lat, lon, error) =
            validateCoordinates(input)

        if (error != null) {
            _state.value = _state.value.copy(error = error)
            return
        }

        checkRainStatus(lat, lon)
    }

    private fun validateCoordinates(
        input: String
    ): Triple<Float, Float, Int?> {

        val parts = input.split(", ")

        val lat = parts.getOrNull(index = 0)?.trim()?.toFloatOrNull()
        val lon = parts.getOrNull(index = 1)?.trim()?.toFloatOrNull()

        val error = when {
            input.isBlank() ->
                R.string.empty_fields_error

            parts.size != 2 ->
                R.string.invalid_format_error

            lat == null || lon == null ->
                R.string.invalid_number_error

            lat !in -90f..90f ||
                    lon !in -180f..180f ->
                R.string.invalid_coordinate_range_error

            else -> null
        }

        return Triple(lat ?: 0f, lon ?: 0f, error)
    }

    fun clearCoordinates() {
        _state.value = CoordinatesState()
    }

    fun clearNavigation() {
        _navigateToRain.value = null
    }
}