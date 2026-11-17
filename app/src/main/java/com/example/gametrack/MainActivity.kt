package com.example.gametrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.gametrack.screens.AddGameScreen
import com.example.gametrack.screens.GameLibraryScreen
import com.example.gametrack.ui.theme.GameTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameTrackTheme {
                var currentScreen by remember {
                    mutableStateOf("library")
                }

                if (currentScreen == "library") {
                    GameLibraryScreen(
                        onAddGame = {
                            currentScreen = "add"
                        }
                    )
                } else {
                    AddGameScreen(
                        onSaveGame = { title, genre, platform, status ->
                            currentScreen = "library"
                        },
                        onCancel = {
                            currentScreen = "library"
                        }
                    )
                }
            }
        }
    }
}
