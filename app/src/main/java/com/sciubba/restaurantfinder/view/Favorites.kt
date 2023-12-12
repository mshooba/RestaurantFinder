
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.data.api.model.Location.Data
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.sciubba.restaurantfinder.ui.theme.TertiaryContainerDark
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Favorites(viewModel: LocationViewModel, navController: NavController) {
    //get the state of the fav rest ids from the viewmodel
    val favoriteRestaurantIds by viewModel.favoriteRestaurantIdsFlow.collectAsState(initial = setOf())
    //filter the restuarnts to only include favorites
    val favoriteRestaurants = viewModel.restaurantList.filter { it.locationId in favoriteRestaurantIds }

//    was trying to add filtering options...
//    val cities = favoriteRestaurants.map { it.addressObj.city }.distinct()
//    val countries = favoriteRestaurants.map { it.addressObj.country }.distinct()

    //manage selected city and country
    var selectedCity by remember { mutableStateOf("") }
   // var expandedCity by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("") }
 //   var expandedCountry by remember { mutableStateOf(false) }


    //state tp update / manage the list of favorites
    var favoriteRestaurantsState = remember { mutableStateListOf<Data>() }
    LaunchedEffect(key1 = favoriteRestaurantIds) {
        favoriteRestaurantsState.clear()
        favoriteRestaurantsState.addAll(favoriteRestaurants)
    }

    // callback for removing a favorite from the list
    val onItemDismissed: (String) -> Unit = { dismissedRestaurantId ->
        favoriteRestaurantsState.removeIf { it.locationId == dismissedRestaurantId }
    }


    Scaffold(
        topBar = {
            //top app bar with a back button
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TertiaryContainerDark
                )
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {


                LazyColumn {
                    items(

                        items = favoriteRestaurants.filter {
                            (selectedCity.isBlank() || it.addressObj.city == selectedCity) &&
                                    (selectedCountry.isBlank() || it.addressObj.country == selectedCountry)
                        },
                        key = { restaurant -> restaurant.locationId }
                    ) { restaurant ->
                        //each rest in the list has a swipe to dismiss
                        FavoriteRestaurantListItem(
                            restaurant = restaurant,
                            viewModel = viewModel ,
                            navigateToDetail = {
                                navController.navigate("restaurantDetail/${restaurant.locationId}")
                            },
                            onDismissed = onItemDismissed
                        )
                        Divider()
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteRestaurantListItem(
    restaurant: Data,
    viewModel: LocationViewModel,
    navigateToDetail: (String) -> Unit,
    onDismissed: (String) -> Unit
) {

    //keep track of dismiss
    val dismissState = rememberDismissState()
    val scope = rememberCoroutineScope()

    if (dismissState.isDismissed(DismissDirection.StartToEnd) || dismissState.isDismissed(DismissDirection.EndToStart)) {
        scope.launch {
            //toggle the favorite status in the viewmodel and use callback
            viewModel.toggleFavoriteRestaurant(restaurant.locationId)
            onDismissed(restaurant.locationId)
            //reset the dismiss state
            dismissState.reset()
        }
    }


    //swqipt to dismiss layout, red color isn't right and animation doesnt look right
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            Background(dismissState = dismissState)
        },
        dismissContent = {
            ListItemContent(restaurant = restaurant, navigateToDetail = navigateToDetail)
        }
    )

    if (dismissState.isDismissed(DismissDirection.StartToEnd) || dismissState.isDismissed(DismissDirection.EndToStart)) {
        scope.launch {
            viewModel.toggleFavoriteRestaurant(restaurant.locationId)
            onDismissed(restaurant.locationId)
            dismissState.reset()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Background(dismissState: DismissState) {
    val color = if (dismissState.dismissDirection != null) Color.Red else Color.Transparent
    val alignment = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Alignment.CenterEnd
        DismissDirection.EndToStart -> Alignment.CenterEnd
        null -> Alignment.Center
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = alignment
    ) {
        if (dismissState.dismissDirection != null) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



@Composable
fun ListItemContent(
    restaurant: Data,
    navigateToDetail: (String) -> Unit
) {
    //layout for each item in the list
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(restaurant.locationId) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //display image
        RestaurantImage(restaurant = restaurant)
        Column(
            //other properties...
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = restaurant.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = restaurant.addressObj.city,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun RestaurantImage(restaurant: Data) {
    GlideImage(
        //use the image api match photo filename to locationID to get the correct image
        imageModel = {"https://ik.imagekit.io/restappimages/locations/${restaurant.locationId}.jpg"},
        modifier = Modifier
            .size(56.dp)
            .aspectRatio(1f)
    )
}

