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

/**
 * Handles the business logic for location-related features. Manages UI state and interactions
 * for fetching and updating restaurant data. Uses FavoriteRestaurantsPreferences to persist
 * user's favorite restaurant selections.
 *
 * Invokes API calls to fetch restaurants, manages favorite selections, and provides necessary data
 * to the UI components for rendering. Survives configuration changes like screen rotations allowing
 * user's favorite restaurants persist without needing to re-fetch from the API.
 */


//datastore for favorited restaurants
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Favorites")

class LocationViewModel(context: Context) : ViewModel() {

    private val favoriteRestaurantsPreferences = FavoriteRestaurantsPreferences(context)
    //hold the ID's of the favorite restaurants
    var favoriteRestaurantIdsFlow: Flow<Set<String>> = flowOf(setOf())

    // State for the currently selected location - used for navigation
    lateinit var clickedLocation: LocationItem

    // Listener for location click events
    val onLocationClicked: (location: LocationItem) -> Unit = {
        clickedLocation = it
    }

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


    init {
        // Called when the ViewModel is created. Initializes the ViewModel state by loading favorites and all restaurants.
        Log.d("LocationViewModel", "Initializing ViewModel")
        loadFavoriteRestaurants()
        loadAllRestaurants()
    }

    private fun loadAllRestaurants() {
        // Launches a coroutine in the ViewModel scope to load all restaurants from the API.
        viewModelScope.launch {
            try {
                val restaurants = apiService.getRestaurants().flatMap { it.data }
                _restaurantList.clear()
                _restaurantList.addAll(restaurants)
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error loading all restaurants: ${e.message}")
            }
        }
    }

    private fun loadFavoriteRestaurants() {
        // Retrieves the set of favorite restaurant IDs and updates the ViewModel state.
        viewModelScope.launch {
            favoriteRestaurantIdsFlow = favoriteRestaurantsPreferences.getFavoriteRestaurants()
        }
    }

    // Retrieves and filters restaurants based on the clicked location's country.
    fun getRestaurants(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val allRestaurants = apiService.getRestaurants().flatMap { it.data }
                _restaurantList.clear()
                if (::clickedLocation.isInitialized) {
                    val filteredRestaurants = allRestaurants.filter {
                        it.addressObj.country == clickedLocation.addressObj.country
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

    // Returns the restaurant details for the given location ID if it exists in the list.
    fun getRestaurantByLocationId(locationId: String): Data? {
        return _restaurantList.firstOrNull { it.locationId == locationId }
    }

    // Fetches all reviews from the API and updates the list of reviews in the ViewModel.
    fun fetchAllReviews() {
        viewModelScope.launch {
            isReviewsFetched = false
            try {
                val reviewsItems = apiService.getAllReviews()
                _allReviewsList.clear()
                reviewsItems.forEach { reviewsItem ->
                    _allReviewsList.addAll(reviewsItem.data)
                }
                isReviewsFetched = true
            } catch (e: Exception) {
                errorMessage = "Error fetching reviews: ${e.message}"
            }
        }
    }

    // Filters and returns reviews for a specific restaurant by location ID.
    fun getReviewsForRestaurant(locationId: String): List<com.sciubba.restaurantfinder.data.api.model.Review.Data> {
        return _allReviewsList.filter { it.locationId.toString() == locationId }
    }

    // Fetches location details and updates the list of locations in the ViewModel.
    fun getLocations() {
        viewModelScope.launch {
            try {
                val locations = apiService.getLocationDetails()
                _locationList.clear()
                _locationList.addAll(locations)
            } catch (e: Exception) {
                errorMessage = "Failed to fetch location details: ${e.message}"
            }
        }
    }

    // Toggles the favorite state of a restaurant by adding or removing it from the favorites list.
    fun toggleFavoriteRestaurant(restaurantId: String) {
        viewModelScope.launch {
            try {
                val currentFavorites = favoriteRestaurantIdsFlow.first().toMutableSet()
                if (currentFavorites.contains(restaurantId)) {
                    currentFavorites.remove(restaurantId)
                } else {
                    currentFavorites.add(restaurantId)
                }
                favoriteRestaurantsPreferences.saveFavoriteRestaurants(currentFavorites)
                refreshFavoriteRestaurantsFlow()
            } catch (e: Exception) {
                errorMessage = "Error toggling favorite restaurant: ${e.message}"
            }
        }
    }

    // Refreshes the flow of favorite restaurant IDs from the data store.
    private fun refreshFavoriteRestaurantsFlow() {
        viewModelScope.launch {
            favoriteRestaurantIdsFlow = favoriteRestaurantsPreferences.getFavoriteRestaurants()
        }
    }

//
}//ViewModel



