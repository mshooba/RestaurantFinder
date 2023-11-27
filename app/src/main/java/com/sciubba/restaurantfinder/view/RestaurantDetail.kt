
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sciubba.restaurantfinder.data.api.model.LocationViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.text.SimpleDateFormat
import java.util.Locale
import com.sciubba.restaurantfinder.data.api.model.Review.Data as ReviewData

@Composable
fun RestaurantDetail(locationId: String?,
                     viewModel: LocationViewModel,
                     onWriteReview: () -> Unit) {
    // Fetch all reviews when the composable is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchAllReviews()
    }

    val restaurant = locationId?.let { viewModel.getRestaurantByLocationId(it) }

    Column {
        // Restaurant Image and Name
        restaurant?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                GlideImage(
                    imageModel = {"https://ik.imagekit.io/restappimages/locations/${restaurant.locationId}.jpg"},
                  //  contentDescription = "Restaurant Image",
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 150f
                            )
                        )
                )

                // Text over the image
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
        }

        // Reviews
        val reviews = locationId?.let { viewModel.getReviewsForRestaurant(it) } ?: listOf()

        if (reviews.isEmpty()) {
            //Text("No reviews available for this restaurant")
            NoReviewsUI(onWriteReview)
        } else {
            LazyColumn {
                itemsIndexed(reviews) { index, review ->
                    ReviewItem(review)

                    // Add a Divider if there is another review below it
                    if (index < reviews.size - 1) {
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: ReviewData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar with GlideImage
            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                review.user.avatar.thumbnail?.let { url ->
                    GlideImage(
                        imageModel = { url },

                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Username and Location
            Column {
                Text(
                    text = review.user.username ?: "Unknown User",
                    style = MaterialTheme.typography.bodyMedium
                )
                review.user.userLocation.name?.let { locationName ->
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Review Title
        Text(
            text = review.title ?: "No Title",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.padding(5.dp))

        // Rating and Published Date
        Row {
//            GlideImage(
//                imageModel = { review.ratingImageUrl }, // Pass the URL directly
//            //    contentDescription = null, // You can set this to null since it's decorative
//                modifier = Modifier.size(24.dp), // Adjust the size as needed
//                loading = {
//                    // Handle loading state (optional)
//                },
//                failure = {
//                    // Handle image loading failure
//                    Text("Image Loading Failed", modifier = Modifier.align(Alignment.Center))
//                }
//            )


            Spacer(modifier = Modifier.width(8.dp))
            review.publishedDate?.let { dateStr ->
                val formattedDate = formatDate(dateStr)
                Text(
                    text = "Published: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.padding(10.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Review Text
        Text(
            text = review.text ?: "No Review Text",
            style = MaterialTheme.typography.bodyMedium
        )

        // Add more details as needed (e.g., subratings, travel date, etc.)
    }
}


@Composable
fun NoReviewsUI(onWriteReview: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hey, this restaurant has no reviews yet. Wanna leave a review?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onWriteReview,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Write a Review")
        }
    }
}

// Function to format date string to a human-readable format
private fun formatDate(dateStr: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.US)

    return try {
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateStr // Return the original string if parsing fails
    }
}