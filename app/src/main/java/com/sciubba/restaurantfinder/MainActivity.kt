package com.sciubba.restaurantfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.sciubba.restaurantfinder.ui.theme.APIHomeworkTheme
import com.sciubba.restaurantfinder.view.HomeScreen
import com.sciubba.restaurantfinder.view.RestaurantList


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APIHomeworkTheme(darkTheme = true) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable(
                            //set the nav route to the restaurant list with args
                            route = "RestaurantList/{locationId}/{latitude}/{longitude}",
                            arguments = listOf(
                                navArgument("locationId") { type = NavType.StringType },
                                navArgument("latitude") { type = NavType.StringType },
                                navArgument("longitude") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val locationId = backStackEntry.arguments?.getString("locationId") ?: ""
                            val latitude = backStackEntry.arguments?.getString("latitude") ?: ""
                            val longitude = backStackEntry.arguments?.getString("longitude") ?: ""


                            // Call RestaurantList with these arguments
                            RestaurantList(
                                restaurantList = listOf(),
                                viewModel = LocationViewModel(),
                                locationId = locationId,
                                latitude = latitude,
                                longitude = longitude

                            )
                        }
                    }
                }
            }
        }
    }
}
