
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRestaurantsPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_restaurants")
    }

    // Define a key for the favorite restaurants
    private val FAVORITE_RESTAURANTS_KEY = stringSetPreferencesKey("favorite_restaurants")

    // Function to retrieve the favorite restaurants
    suspend fun getFavoriteRestaurants(): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[FAVORITE_RESTAURANTS_KEY] ?: setOf()
        }
    }

    // Function to add a restaurant to favorites
    suspend fun addFavoriteRestaurant(restaurantId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_RESTAURANTS_KEY] ?: setOf()
            preferences[FAVORITE_RESTAURANTS_KEY] = currentFavorites + restaurantId
        }
    }

    // Function to remove a restaurant from favorites
    suspend fun removeFavoriteRestaurant(restaurantId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_RESTAURANTS_KEY] ?: setOf()
            preferences[FAVORITE_RESTAURANTS_KEY] = currentFavorites - restaurantId
        }
    }
}
