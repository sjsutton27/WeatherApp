package com.example.weatherapp.presentation.screens.rain

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.domain.use_case.GetRainStatusUseCase
import com.example.weatherapp.presentation.view_model.WeatherViewModel
import com.example.weatherapp.presentation.view_model.viewModelFactory
import com.example.weatherapp.ui.theme.RainColor
import com.example.weatherapp.ui.theme.SunnyColor
import com.example.weatherapp.R
import com.example.weatherapp.data.responses.Coord


@Composable
fun RainScreen(
    navController: NavController,
    coord: Coord,
    viewModel: WeatherViewModel = viewModel(
        factory = viewModelFactory {
            WeatherViewModel(
                useCase = GetRainStatusUseCase(
                    repository = WeatherApplication.appModule.weatherRepository
                )
            )
        }
    )
) {

    val state = viewModel.state.value

    val isLoading = state.isLoading
    val error = state.error
    val isRaining = state.isRaining == true

    val backgroundColor =
        if (isRaining) RainColor else SunnyColor

    val weatherImage =
        if (isRaining)
            R.drawable.rain_image
        else
            R.drawable.sunny_image

    val weatherText =
        if (isRaining)
            stringResource(R.string.raining)
        else
            stringResource(R.string.not_raining)

    LaunchedEffect(Unit) {
        viewModel.checkRainStatus(coord.lat, coord.lon)
    }

    BackHandler {
        navController.popBackStack()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(all = 16.dp)
        ) {

            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(weatherImage),
                    contentDescription = "Rain illustration",
                    modifier = Modifier.size(160.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Lat: ${coord.lat}, Lon: ${coord.lon}",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = weatherText,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                if (isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                error?.let {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.rain_screen_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}