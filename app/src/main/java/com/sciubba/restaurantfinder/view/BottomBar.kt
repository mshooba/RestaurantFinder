
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(
    onHomeClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    onAboutClicked: () -> Unit,
    selectedTab: String
) {
    BottomAppBar(

    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Home Icon
        val isHomeSelected = selectedTab == "home"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    onHomeClicked()
                }
            ) {
                Icon(
                    imageVector = if (isHomeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = if (isHomeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary // Conditional tint

                )
            }
            Text(
                text = "Home",
                style = MaterialTheme.typography.labelSmall,
                color = if (isHomeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary // Conditional tint
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Favorites Icon
        val isFavoritesSelected = selectedTab == "favorites"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    onFavoriteClicked()
                }
            ) {
                Icon(
                    imageVector = if (isFavoritesSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorites",
                    tint = if (isFavoritesSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.labelSmall,
                color = if (isFavoritesSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // About Icon
        val isAboutSelected = selectedTab == "about"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    onAboutClicked()
                }
            ) {
                Icon(
                    imageVector = if (isAboutSelected) Icons.Filled.Info else Icons.Outlined.Info,
                    contentDescription = "About",
                    tint = if (isAboutSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

                )
            }
            Text(
                text = "About",
                style = MaterialTheme.typography.labelSmall,
                color = if (isAboutSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
