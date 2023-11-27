package com.sciubba.restaurantfinder.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.data.api.model.Location.Data
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RestaurantList(
    viewModel: LocationViewModel,
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->
        if (viewModel.restaurantList.isNotEmpty()) {
            LazyColumn(contentPadding = paddingValues) {
                items(viewModel.restaurantList) { restaurant ->
                    RestaurantListItem(
                        viewModel = viewModel,
                        restaurant = restaurant,
                        navigateToDetail = { locationId ->
                            navController.navigate("restaurantDetail/$locationId")
                        },
                        snackbarHostState = snackbarHostState
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        thickness = 1.dp
                    )
                }
            }
        } else if (viewModel.isRestaurantsFetched) {
            Text("No restaurants available.")
        } else {
            Text("Fetching restaurants...")
        }
    }
}


@Composable
fun RestaurantListItem(
    viewModel: LocationViewModel,
    restaurant: Data,
    navigateToDetail: (String) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var isFavorite by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(restaurant.locationId) }

            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image on the left
        GlideImage(
            imageModel = {"https://ik.imagekit.io/restappimages/locations/${restaurant.locationId}.jpg"},
            loading = {
                // Your loading composable here
            },
            failure = {
                Text("Image Request Failed", modifier = Modifier.align(Alignment.Center))
            },
            modifier = Modifier
                .size(88.dp) // Fixed size for the image, adjust as needed
                .aspectRatio(1f) // Makes the image square, adjust as needed
        )

        // Text and details on the right
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = restaurant.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Address",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = restaurant.addressObj.street1 ?: "",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = restaurant.addressObj.city ?: "",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )


        }//Column

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite",
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    isFavorite = !isFavorite
                    viewModel.toggleFavoriteRestaurant(restaurant.locationId)
                    val message = if (isFavorite) {
                        "${restaurant.name} Added to Favorites"
                    } else {
                        "${restaurant.name} Removed from Favorites"
                    }
                    // Trigger snackbar display
                    CoroutineScope(Dispatchers.Main).launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Short

                        )
                    }
                }
        )


    }//Row
}


@Composable
fun AddToFavoritesButton(onClick: () -> Unit) {
    // You can style this button as needed
    Button(
        onClick = onClick,
        modifier = Modifier.padding(start = 8.dp),
    ) {
        Text(text = "Add to Favorites")
    }
}



