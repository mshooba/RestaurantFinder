
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sciubba.restaurantfinder.ui.theme.Typography

@Composable
fun About(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        item {
            // Project introduction
            Text(
                text = "Welcome to Restaurant Finder!",
                style = Typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Discover the Best Restaurants in the World",
                style = Typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "A Small Version of TripAdvisor",
                style = Typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Explore top restaurants in the best food cities around the globe. The API, powered by Koyeb and TripAdvisor, provides you with access to a curated list of restaurants with stunning photos and reviews.",
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
            )




        }

        // Tutorial section
        item {
            Text(
                text = "Tutorials Followed:",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Unsplash Image Attribution:",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(8.dp)
            )



        }

        // Add your tutorial links here
        val tutorialLinks = listOf(
            "Custom Themes" to "https://www.geeksforgeeks.org/how-to-create-a-dark-mode-for-a-custom-android-app-in-kotlin/",
            "Express Js tutorial" to "https://developer.mozilla.org/en-US/docs/Learn/Server-side/Express_Nodejs/Introduction",
            "Bottom App Bar in Android using Kotlin" to "https://medium.com/@myofficework000/a-step-by-step-guide-to-implementing-bottom-app-bar-in-android-using-kotlin-4ab7487bb32d"
        )


        tutorialLinks.forEach { (title, link) ->
            item {
                Text(
                    text = title,
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            // Handle click and open the link
                            // Open the tutorial link
                            // You can use the link variable here to open the URL
                        }
                )
            }
        }



        // Create a scrollable list of photographers
        val unsplashPhotographers = listOf(
            "Yohan Marion",
            "Sabrina Mazzeo",
            "Todd Diemer",
            "Nick Karvounis",
            "Irina",
            "Raul Croes",
            "Zita Chan",
            "Big Dodzy",
            "K-Soma",
            "Jeanna Rose",
            "Fransisco Suarez",
            "Luca Bravo",
            "Robert Bye",
            "Vernon Raine",
            "Alice",
            "Diego Marin",
            "Gerrie Vanderwall",
            "Anna Lanza",
            "Tim Mossholder",
            "Paul Griffin",
            "Nathalia Segato",
            "Crystal Jo"
        )

        unsplashPhotographers.forEach { photographer ->
            item {
                Row(
                    modifier = Modifier
                        .clickable { /* Handle click action */ }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = photographer,
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.ColorLens,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
