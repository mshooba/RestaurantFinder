package com.sciubba.restaurantfinder.ui
import About
import BottomBar
import Favorites
import RestaurantDetail
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: LocationViewModel = viewModel { LocationViewModel(applicationContext) }

            viewModel.getLocations()

            APIHomeworkTheme(darkTheme = true) {
                // State to track the current screen
                var currentScreen by remember { mutableStateOf("home") }
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(
                            onHomeClicked = {
                                currentScreen = "home"
                                navController.navigate("home")
                            },
                            onFavoriteClicked = {
                                currentScreen = "favorites"
                                navController.navigate("favorites")
                            },
                            onAboutClicked = {
                                currentScreen = "about"
                                navController.navigate("about")
                            },
                            selectedTab = currentScreen // Pass the current screen to BottomBar
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(navController, viewModel)
                        }
                        composable("favorites") {
                            Favorites(viewModel = viewModel, navController = navController)

                        }
                        composable("about") {
                            About(navController)
                        }
                        composable("RestaurantList") {
                            RestaurantList(viewModel = viewModel, navController = navController)
                        }

                        composable(
                            route = "restaurantDetail/{locationId}",
                            arguments = listOf(navArgument("locationId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Retrieve the locationId from the arguments
                            val locationId = backStackEntry.arguments?.getString("locationId")
                            // You can now use this ID to get the details from your ViewModel
                            RestaurantDetail(
                                locationId,
                                viewModel,
                                onWriteReview = {
                                    // Define what should happen when the "Write a Review" button is clicked.
                                    // For example, you can navigate to a review writing screen here.
                                }
                            )
                        }
                        // Listen for navigation changes and update currentScreen
                        navController.addOnDestinationChangedListener { _, destination, _ ->
                            currentScreen = destination.route ?: "home"
                        }
                    }
                }
            }
        }
    }
}
