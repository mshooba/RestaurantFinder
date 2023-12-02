
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.data.api.model.Location.Data
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import kotlinx.coroutines.launch

// Inside Favorites.kt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(viewModel: LocationViewModel, navController: NavController) {
    val favoriteRestaurantIds by viewModel.favoriteRestaurantIdsFlow.collectAsState(initial = setOf())
    val favoriteRestaurants = viewModel.restaurantList.filter { it.locationId in favoriteRestaurantIds }

    // Extract unique cities and countries
    val cities = favoriteRestaurants.map { it.addressObj.city }.distinct()
    val countries = favoriteRestaurants.map { it.addressObj.country }.distinct()

    var selectedCity by remember { mutableStateOf("") }
    var expandedCity by remember { mutableStateOf(false) }

    var selectedCountry by remember { mutableStateOf("") }
    var expandedCountry by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favorites") })
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ExposedDropdownMenuBox(
                    expanded = expandedCity,
                    onExpandedChange = { expandedCity = !expandedCity }
                ) {
                    TextField(
                        value = selectedCity,
                        onValueChange = { selectedCity = it },
                        label = { Text("Select City") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCity) }
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCity,
                        onDismissRequest = { expandedCity = false }
                    ) {
                        cities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city) },
                                onClick = {
                                    selectedCity = city
                                    expandedCity = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedCountry,
                    onExpandedChange = { expandedCountry = !expandedCountry }
                ) {
                    TextField(
                        value = selectedCountry,
                        onValueChange = { selectedCountry = it },
                        label = { Text("Select Country") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCountry) }
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCountry,
                        onDismissRequest = { expandedCountry = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text(country) },
                                onClick = {
                                    selectedCountry = country
                                    expandedCountry = false
                                }
                            )
                        }
                    }
                }

                val filteredRestaurants = favoriteRestaurants.filter {
                    (selectedCity.isBlank() || it.addressObj.city == selectedCity) &&
                            (selectedCountry.isBlank() || it.addressObj.country == selectedCountry)
                }

                LazyColumn {
                    items(
                        items = filteredRestaurants,
                        key = { restaurant -> restaurant.locationId }
                    ) { restaurant ->
                        FavoriteRestaurantListItem(
                            restaurant = restaurant,
                            viewModel = viewModel,
                            navigateToDetail = { locationId ->
                                navController.navigate("restaurantDetail/$locationId")
                            }
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun DropDownMenu(
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
            .padding(8.dp)
    ) {
        Text(
            text = selectedItem ?: label,
            modifier = Modifier.clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteRestaurantListItem(
    restaurant: Data,
    viewModel: LocationViewModel,

    navigateToDetail: (String) -> Unit,// Pass the ViewModel to the composable
) {
    val dismissState = rememberDismissState()
    val scope = rememberCoroutineScope()

    if (dismissState.isDismissed(DismissDirection.StartToEnd) || dismissState.isDismissed(DismissDirection.EndToStart)) {
        scope.launch {
            viewModel.toggleFavoriteRestaurant(restaurant.locationId)
            dismissState.reset()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> Color.Green
                DismissDirection.EndToStart -> Color.Red
                null -> Color.Transparent // No swipe detected
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color)
                    .padding(16.dp)
                    .clickable { navigateToDetail(restaurant.locationId) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),

                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        dismissContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = restaurant.addressObj.city,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )


            }

        },


    )
}