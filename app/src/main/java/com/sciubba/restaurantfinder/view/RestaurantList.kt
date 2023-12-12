package com.sciubba.restaurantfinder.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.data.api.model.Location.Data
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.sciubba.restaurantfinder.ui.theme.OnPrimaryLight
import com.sciubba.restaurantfinder.ui.theme.TertiaryContainerDark
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantList(
    viewModel: LocationViewModel,
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Restaurants",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth(),
                           // .wrapContentWidth(Alignment.CenterHorizontally),

                        color = OnPrimaryLight
                    )
                },
                navigationIcon = {
                    //navigate to the homescreen on click
                    IconButton(onClick = {navController.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Localized description"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TertiaryContainerDark
                )
                // add nav drawer later? probably bottom app bar instead
            )
        }, // topBar
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
    val favoriteRestaurantIds by viewModel.favoriteRestaurantIdsFlow.collectAsState(initial = setOf())
    val isFavorite = restaurant.locationId in favoriteRestaurantIds

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(restaurant.locationId) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Image on the left
        GlideImage(
            //use the image api to get the appropriate ime
            imageModel = {"https://ik.imagekit.io/restappimages/locations/${restaurant.locationId}.jpg"},
            loading = {
                // Your loading composable here
            },
            failure = {
                //didn't work
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
                .weight(5f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = restaurant.name ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
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

        Box(
            modifier = Modifier
                .padding(start = 8.dp) // Add padding to ensure space between text and icon
                .size(24.dp) // Fixed size for the icon
        ) {


            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center)
                    .clickable {
                        viewModel.toggleFavoriteRestaurant(restaurant.locationId)
                        val updatedFavoriteState = !isFavorite
                        val message = if (updatedFavoriteState) {
                            "${restaurant.name} Added to Favorites"
                        } else {
                            "${restaurant.name} Removed from Favorites"
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
            )
        }

    }//Row
}





