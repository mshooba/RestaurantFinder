package com.sciubba.restaurantfinder.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.data.api.model.Location.LocationItem
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.sciubba.restaurantfinder.ui.theme.OnPrimaryLight
import com.sciubba.restaurantfinder.ui.theme.TertiaryContainerDark
import com.skydoves.landscapist.glide.GlideImage


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,
               viewModel: LocationViewModel  )
{
   // val viewModel: LocationViewModel = viewModel()


    Scaffold (

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Restaurant Finder",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                           // .padding(top = 20.dp),
                        color = OnPrimaryLight
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TertiaryContainerDark
                )
                // add nav drawer later? probably bottom app bar instead
            )
        }, // topBar

        content = {
            if (viewModel.errorMessage.isEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    // Check if the location list is empty
                    if (viewModel.locationList.isEmpty()) {
                        item {
//                            CircularProgressIndicator(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .wrapContentSize(align = Alignment.Center)
//                            )
                        }
                    } else {
                        items(viewModel.locationList) { location ->

                                LocationCard(
                                    navController = navController,
                                    location = location,
                                    viewModel = viewModel

                                )


                        }
                    }

                }
            } else {
                //show the message
                Text(viewModel.errorMessage)
            }
        }) //content
}//HomeScreen

@Composable
fun LocationCard(navController: NavController,
                 location: LocationItem,
                 viewModel: LocationViewModel

) {

    //handle the weburl for a location
    val uriHandler = LocalUriHandler.current
    //state for exapanding the item description
    var isExpanded by remember { mutableStateOf(false) }
  //  val viewModel: LocationViewModel = viewModel()

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 28.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            GlideImage(
                //get the image for each location
                imageModel = {"https://ik.imagekit.io/restappimages/locations/${location.locationId}.jpg"},
                // Sets the aspect ratio to maintain across different image sizes
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f) // 16:9 aspect ratio, adjust as needed
                    .padding(bottom = 16.dp),
                loading = {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.Center)
//                    )
                },
                // Provide the failure composable to be shown on image request failure
                failure = {
                    Text("Image Request Failed", modifier = Modifier.align(Alignment.Center))
                }
            )//GlideImage

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn, // This is the Material icon for location
                    contentDescription = "Location",

                    modifier = Modifier.size(24.dp) // Set the size of the icon as needed
                )//location icon

                // Add space between the icon and text
                Spacer(Modifier.width(8.dp))

                Text(
                    text = location.addressObj.addressString,
                    style = MaterialTheme.typography.titleLarge
                )//location title

                //add space between the location and description
                Spacer(Modifier.width(24.dp))
            }//Row

            //the location descriotion
            val description = location.description
            //trim the description
            val trimmedDescription = if (description.length > 100 && !isExpanded) {
                "${description.take(100)}..."
            } else {
                description
            }

            Text(
                text = trimmedDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { isExpanded = !isExpanded } // toggle isExpanded when text is clicked
            )

            if (description.length > 100) {
                Text(
                    text = if (isExpanded) "...read Less" else "...read more",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { isExpanded = !isExpanded } // toggle the isExpanded when text is clicked
                )
            }


            Spacer(modifier = Modifier
                    .padding(10.dp))
            // You can add more properties here
            Text(
                text = "Timezone: ${location.timezone}",
                style = MaterialTheme.typography.bodySmall
            )
            // Add a clickable web link if needed
            if (location.webUrl.isNotEmpty()) {
                Text(
                    text = "More Info",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            // Handle the click to open the web URL
                            uriHandler.openUri(location.webUrl) //
                        }
                        .padding(top = 8.dp)
                )
            }

          //  val onBrowseRestaurantsClicked = false
            Button(
                onClick = {
                    viewModel.onLocationClicked(location) // Set the clicked location
                    viewModel.getRestaurants {
                        navController.navigate("RestaurantList")
                    }

                    },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Adjust padding as needed
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Browse Restaurants")
            }
        }//Column
    }
}//LocationCard

