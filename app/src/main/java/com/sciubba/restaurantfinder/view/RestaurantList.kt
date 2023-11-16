package com.sciubba.restaurantfinder.view

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sciubba.restaurantfinder.data.api.model.AddressObj
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.sciubba.restaurantfinder.data.api.model.NearbyLocation
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun RestaurantList(
    restaurantList: List<NearbyLocation>,
    viewModel: LocationViewModel = viewModel(),
    locationId: String,
    latitude: String,
    longitude: String
) {
    Log.d("RestaurantList", "Displaying RestaurantList for locationId: $locationId, latitude: $latitude, longitude: $longitude")
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = latitude, key2 = longitude) {
        viewModel.fetchNearbyRestaurants(latitude, longitude)
    }

    LazyColumn(state = listState) {
        items(viewModel.nearbyRestaurants) { restaurant ->
            RestaurantListItem(restaurant = restaurant)
        }
    }
}

@Composable
fun RestaurantListItem(restaurant: NearbyLocation) {

    Text(text = restaurant.name)
    Row(
        modifier = Modifier
            .fillMaxWidth() //make the whole row clickable
            .clickable {

            }
            .padding(all = 8.dp), //padding around the rows
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {

            Row {
                GlideImage(
                    imageModel = {},
                )//Image

                //space between image and column (title and detail)
                Spacer(modifier = Modifier.width(8.dp))

                //Keep track of if the card is expanded or not
                var isExpanded by remember { mutableStateOf(false) }

                //"animate" the container for the expanded / not expanded
                val surfaceColor: Color by animateColorAsState(
                    if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    label = "",

                    )

                Column(
                    modifier = Modifier.clickable {
                        isExpanded = !isExpanded
                    }
                ) {
                    Text(
                        text = "Restaurant Name",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 1.dp,
                        color = surfaceColor, //gradually change from primary to surface
                        modifier = Modifier
                            .animateContentSize()
                            .padding(1.dp)
                    ) {
                        Text(
                            text = "Other Info",
                            //if expanded display all the content, if not only one line
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }//Surface
                }//Column
            }//Row for image text and column
        }//


    }
}

@Preview
@Composable
fun RestaurantListItemPreview() {
    RestaurantListItem(
        restaurant = NearbyLocation(
            locationId = "",
            name = "",
            distance = "",
            bearing = "",
            addressObj = AddressObj(
                addressString = "",
                city = "",
                country = ""
            )
        )
    )
}

@Preview
@Composable
fun RestaurantListPreview() {
    RestaurantList(
        restaurantList = listOf(),
        viewModel = LocationViewModel(),
        locationId = "",
        latitude = "",
        longitude = ""
    )
}

