package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.weatherapp.data.responses.Coord
import com.example.weatherapp.presentation.screens.coordinates.CoordinatesScreen
import com.example.weatherapp.presentation.screens.rain.RainScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherNavGraph()
            }
        }
    }
}

@Composable
fun WeatherNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "coordinates_screen"
    ) {

        composable("coordinates_screen") {
            CoordinatesScreen(
                navController = navController,
            )
        }

        composable(
            route = "rain_screen/{lat}/{lon}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType }
            )
        ) { backStackEntry ->

            val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
            val lon = backStackEntry.arguments?.getFloat("lon") ?: 0f

            RainScreen(
                navController = navController,
                coord = Coord(lat, lon)
            )
        }
    }
}