package com.sciubba.apihomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sciubba.apihomework.ui.HomeScreen
import com.sciubba.apihomework.ui.theme.APIHomeworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APIHomeworkTheme(darkTheme = true) { // Force dark theme - custom theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HomeScreen()
                }
            }//Theme
        }//setContent
    }//onCreate
}//MainActivity

