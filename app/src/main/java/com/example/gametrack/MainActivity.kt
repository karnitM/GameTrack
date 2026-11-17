package com.example.gametrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gametrack.screens.GameLibraryScreen
import com.example.gametrack.ui.theme.GameTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameTrackTheme {
                GameLibraryScreen()
            }
        }
    }
}
