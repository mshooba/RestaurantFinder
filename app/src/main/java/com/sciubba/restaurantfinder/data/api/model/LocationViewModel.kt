package com.sciubba.restaurantfinder.data.api.model
import APIService
import FavoriteRestaurantsPreferences
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sciubba.restaurantfinder.data.api.model.Location.Data
import com.sciubba.restaurantfinder.data.api.model.Location.LocationItem
import com.sciubba.restaurantfinder.data.api.model.Review.ReviewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


//datastore for favorited restaurants
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Favorites")

class LocationViewModel(context: Context) : ViewModel() {

    //private val _allFavoriteRestaurantDetails = mutableStateListOf<Data>()
    private val favoriteRestaurantsPreferences = FavoriteRestaurantsPreferences(context)
    //hold the ID's of the favorite restaurants
    var favoriteRestaurantIdsFlow: Flow<Set<String>> = flowOf(setOf())

    init {
        Log.d("LocationViewModel", "Initializing ViewModel")
        //load restauarants and favorites when the app loads
        loadFavoriteRestaurants()
        loadAllRestaurants()
    }

    private fun loadAllRestaurants() {
        viewModelScope.launch {
            try {
                Log.d("LocationViewModel", "Loading all restaurants")
                //use the api
                val restaurants = apiService.getRestaurants().flatMap { it.data }
                _restaurantList.clear()
                _restaurantList.addAll(restaurants)
                Log.d("LocationViewModel", "Loaded all restaurants: ${_restaurantList.size}")
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error loading all restaurants: ${e.message}")
            }
        }
    }

    private fun loadFavoriteRestaurants() {
        viewModelScope.launch {
            favoriteRestaurantIdsFlow = favoriteRestaurantsPreferences.getFavoriteRestaurants()
            Log.d("LocationViewModel", "Favorites loaded: ${favoriteRestaurantIdsFlow.first()}")
        }
    }


    // State for the currently selected location
    lateinit var clickedLocation: LocationItem

    // Listener for location click events
    val onLocationClicked: (location: LocationItem) -> Unit = {
        clickedLocation = it
    }

    // State for favorite restaurants (list of restaurant IDs)
    private val _favoriteRestaurantIds = mutableStateListOf<String>()



    // State for locations
    private val _locationList = mutableStateListOf<LocationItem>()
    val locationList: List<LocationItem> get() = _locationList

    // State for fetching status of restaurants
    var isRestaurantsFetched by mutableStateOf(false)

    // State for the list of restaurants
    private val _restaurantList = mutableStateListOf<Data>()
    val restaurantList: List<Data> get() = _restaurantList

    // State for reviews
    private val _reviewsList = mutableStateListOf<ReviewsItem>()
    val reviewsList: List<ReviewsItem> get() = _reviewsList

    // State for all reviews
    private val _allReviewsList = mutableStateListOf<com.sciubba.restaurantfinder.data.api.model.Review.Data>()
    var isReviewsFetched by mutableStateOf(false)

    // API service instance
    var apiService = APIService.getInstance()

    // Error message state
    var errorMessage: String by mutableStateOf("")

    private val locationIds = listOf(
        //paris
        "187147",
        //rome
        "187791",
        //tokyo
        "298184",
        //ny
        "60763",
        //copenhagen
        "189541",
        //hong kong
        "294217"
    ) // Add more IDs ?


    // Fetch all restaurant details and store them in _restaurantList
    fun getRestaurants(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val allRestaurants = apiService.getRestaurants().flatMap { it.data }

                // Clear the current restaurant list
                _restaurantList.clear()

                // Filter and add restaurants that match the country of the clicked location
                if (::clickedLocation.isInitialized) {
                    val locationCountry = clickedLocation.addressObj.country
                    val filteredRestaurants = allRestaurants.filter {
                        it.addressObj.country == locationCountry
                    }
                    _restaurantList.addAll(filteredRestaurants)
                }

                isRestaurantsFetched = true
            } catch (e: Exception) {
                errorMessage = "Error fetching restaurants: ${e.message}"
            } finally {
                onComplete?.invoke()
            }
        }
    }



    fun getRestaurantByLocationId(locationId: String): Data? {
        return _restaurantList.firstOrNull { it.locationId == locationId }
    }

    fun fetchAllReviews() {
        viewModelScope.launch {
            isReviewsFetched = false
            try {
                val reviewsItems = apiService.getAllReviews() // Fetches a list of ReviewsItem
                _allReviewsList.clear()

                // Add all Data (individual reviews) to _allReviewsList
                reviewsItems.forEach { reviewsItem ->
                    _allReviewsList.addAll(reviewsItem.data) // Add individual reviews
                }

                isReviewsFetched = true
            } catch (e: Exception) {
                errorMessage = "Error fetching reviews: ${e.message}"
            }
        }
    }



    // Function to filter reviews for a specific restaurant
    fun getReviewsForRestaurant(locationId: String): List<com.sciubba.restaurantfinder.data.api.model.Review.Data> {
        return _allReviewsList.filter { it.locationId.toString() == locationId }
    }

    fun getLocations() {
        viewModelScope.launch {
            apiService = APIService.getInstance()
            try {
                val locations = apiService.getLocationDetails() // This should return List<LocationItem>
                _locationList.clear()
                _locationList.addAll(locations)
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Failed to fetch location details", e)
                errorMessage = "Failed to fetch location details: ${e.message}"
            }
        }
    }

    //favorite functionality

    fun toggleFavoriteRestaurant(restaurantId: String) {
        viewModelScope.launch {
            try {
                val currentFavorites = favoriteRestaurantIdsFlow.first().toMutableSet()
                val wasFavorite = currentFavorites.contains(restaurantId)

                if (wasFavorite) {
                    currentFavorites.remove(restaurantId)
                    favoriteRestaurantsPreferences.removeFavoriteRestaurant(restaurantId)
                } else {
                    currentFavorites.add(restaurantId)
                    favoriteRestaurantsPreferences.addFavoriteRestaurant(restaurantId)
                }

                favoriteRestaurantsPreferences.saveFavoriteRestaurants(currentFavorites)
                favoriteRestaurantIdsFlow = favoriteRestaurantsPreferences.getFavoriteRestaurants()



                Log.d("LocationViewModel", "Toggled favorite restaurant: $restaurantId")
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error toggling favorite restaurant: ${e.message}")
            }
        }
    }

    private fun updateRestaurantListBasedOnFavorites(favoriteIds: Set<String>) {
        viewModelScope.launch {
            // Assuming apiService.getRestaurants() fetches the full list of restaurants
            val allRestaurants = apiService.getRestaurants().flatMap { it.data }
            _restaurantList.clear()
            _restaurantList.addAll(allRestaurants.filter { it.locationId in favoriteIds })
            Log.d("LocationViewModel", "Updated restaurant list based on favorites")
        }
    }



}//ViewModel



