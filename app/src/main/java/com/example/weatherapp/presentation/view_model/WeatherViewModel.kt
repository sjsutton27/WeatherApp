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

    // ✅ navigation event (fixes double click issue)
    private val _navigateToRain = mutableStateOf<Coord?>(null)
    val navigateToRain: State<Coord?> = _navigateToRain

    fun checkRainStatus(lat: Float, lon: Float) {

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {

            when (val result = useCase(lat, lon)) {

                is Resource.Success -> {

                    val coord = Coord(lat, lon)

                    _state.value = _state.value.copy(
                        coordinates = coord,
                        isRaining = result.data,
                        isLoading = false,
                        error = null
                    )

                    // ✅ trigger navigation ONLY when data is ready
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

        if (input.isBlank()) {
            _state.value = _state.value.copy(
                error = R.string.empty_fields_error
            )
            return
        }

        val parts = input.split(", ")

        if (parts.size != 2) {
            _state.value = _state.value.copy(
                error = R.string.invalid_format_error
            )
            return
        }

        val lat = parts[0].trim().toFloatOrNull()
        val lon = parts[1].trim().toFloatOrNull()

        if (lat == null || lon == null) {
            _state.value = _state.value.copy(
                error = R.string.invalid_number_error
            )
            return
        }

        if (lat !in -90f..90f || lon !in -180f..180f) {
            _state.value = _state.value.copy(
                error = R.string.invalid_coordinate_range_error
            )
            return
        }

        checkRainStatus(lat, lon)
    }

    fun clearCoordinates() {
        _state.value = CoordinatesState()
    }

    fun clearNavigation() {
        _navigateToRain.value = null
    }
}