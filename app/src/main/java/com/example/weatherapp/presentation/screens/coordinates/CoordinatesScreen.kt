package com.example.weatherapp.presentation.screens.coordinates

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.domain.use_case.GetRainStatusUseCase
import com.example.weatherapp.presentation.view_model.WeatherViewModel
import com.example.weatherapp.presentation.view_model.viewModelFactory
import com.example.weatherapp.ui.theme.CoordinateScreenColor

@Composable
fun CoordinatesScreen(
    navController: NavController,
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

    var coordinatesInput by remember { mutableStateOf("") }

    val state = viewModel.state.value
    val navigateCoord = viewModel.navigateToRain.value

    LaunchedEffect(Unit) {
        viewModel.clearCoordinates()
    }

    LaunchedEffect(navigateCoord) {
        navigateCoord?.let { coord ->

            if (!state.isLoading && state.error == null && state.isRaining != null) {

                navController.navigate(
                    "rain_screen/${coord.lat}/${coord.lon}"
                )

                viewModel.clearNavigation()
            }
        }
    }

    Surface(
        color = CoordinateScreenColor,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.rain_splash_screen),
                contentDescription = "Weather illustration",
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = coordinatesInput,
                    onValueChange = {input -> coordinatesInput = input },
                    label = {
                        Text(stringResource(R.string.coordinates_label))
                    },
                    placeholder = {
                        Text(stringResource(R.string.coordinates_placeholder))
                    },
                    singleLine = true,
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        viewModel.onCoordinatesEntered(input = coordinatesInput)
                    },
                    enabled = !state.isLoading
                ) {
                    Text(text = stringResource(R.string.go))
                }
            }

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(12.dp))

            state.error?.let { errorResId ->
                Text(
                    text = stringResource(id = errorResId),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.coordinates_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}