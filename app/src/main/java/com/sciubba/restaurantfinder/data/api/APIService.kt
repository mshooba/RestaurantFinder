
import com.sciubba.restaurantfinder.data.api.model.Location
import com.sciubba.restaurantfinder.data.api.model.NearbyLocation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// The BASE_URL should be just the base path
const val BASE_URL = "https://api.content.tripadvisor.com/api/v1/"

interface APIService {

    //GETTER FOR THE LOCATION
    @GET("location/{locationId}/details")
    suspend fun getLocationDetails(
        @Path("locationId") locationId: String,
        @Query("key") apiKey: String, // No default value here
        @Query("language") language: String = "en",
        @Query("currency") currency: String = "USD"
    ): Location

    @GET("location/nearby_search")
    suspend fun getNearbyRestaurants(
        @Query("latLong") latLong: String,
        @Query("category") category: String = "restaurants",
        @Query("key") apiKey: String,

    ): List<NearbyLocation>

    companion object {
        private var apiService: APIService? = null

        //could not make connection to API without using logging interceptor and OKHttp stuff
        fun getInstance(): APIService {
            if (apiService == null) {
                // Use logger to see HTTP errors and such
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BASIC) // Change to Level.BODY for more details

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
