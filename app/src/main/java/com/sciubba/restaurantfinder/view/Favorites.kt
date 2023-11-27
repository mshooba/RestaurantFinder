
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.data.api.model.Location.Data
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel

@Composable
fun Favorites(viewModel: LocationViewModel, navController: NavController) {
    // Collect the flow of favorite restaurant IDs as state
    val favoriteRestaurantIds by viewModel.favoriteRestaurantIdsFlow.collectAsState(initial = setOf())

    // Display the favorite restaurants based on favorite IDs
    LazyColumn {
        items(viewModel.restaurantList.filter { it.locationId in favoriteRestaurantIds }) { restaurant ->
            FavoriteRestaurantListItem(
                restaurant = restaurant,
                onDismiss = {
                    // Remove from favorites when an item is dismissed
                  //  viewModel.r
                }
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun FavoriteRestaurantListItem(
    restaurant: Data,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onDismiss) //delete the favorites from here?
    ) {



        Text(
            text = restaurant.name ?: "",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = restaurant.addressObj.street1 ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = restaurant.addressObj.city ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )


    }
}

