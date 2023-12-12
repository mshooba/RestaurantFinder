
import com.sciubba.restaurantfinder.data.api.model.Location.Location
import com.sciubba.restaurantfinder.data.api.model.Location.LocationItem
import com.sciubba.restaurantfinder.data.api.model.Location.RestaurantItem
import com.sciubba.restaurantfinder.data.api.model.Review.ReviewsItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// The BASE_URL should be just the base path
//const val BASE_URL = "https://api.content.tripadvisor.com/api/"

//new base url for the koyeb API
const val BASE_URL = "https://eastern-rubie-mattsciubba.koyeb.app/api/"

interface APIService {


    @GET("locationdetails")
    suspend fun getLocationDetails(): List<LocationItem>

    @GET("locations")
    suspend fun getLocations(): Location

    @GET("restaurants")
    suspend fun getRestaurants(): List<RestaurantItem>

    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewsItem>

    companion object {
        private var apiService: APIService? = null

        fun getInstance(): APIService {
            if (apiService == null) {
                // Use logger to see HTTP errors
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                // Change to Level.BODY for more details

                // Create a custom OkHttpClient with a new interceptor for the Referer header
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging) // Add the logging interceptor to OkHttpClient
                    .addInterceptor { chain -> // Add a new interceptor for the Referer header
                        val originalRequest = chain.request()
                        val updatedRequest = originalRequest.newBuilder()
                            .header("Referer", "https://www.mattsciubba.com")
                            .build()
                        chain.proceed(updatedRequest)
                    }
                    .build()

                //init retrofit
                apiService = Retrofit.Builder()
                    //use the base url
                    .baseUrl(BASE_URL)
                    //set client to use w retrofit
                    .client(client)
                    //serialize / deserialize
                    .addConverterFactory(GsonConverterFactory.create())
                    //construct w options
                    .build()
                    //create implementation of the interface to use endpoints
                    .create(APIService::class.java)
            }

            //use it to make API calls
            return apiService!!
        }//getInstance


    }//companion object
}//APIService
