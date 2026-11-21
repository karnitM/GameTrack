package com.example.gametrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gametrack.data.AppDatabase
import com.example.gametrack.repository.GameRepository
import com.example.gametrack.screens.AddGameScreen
import com.example.gametrack.screens.GameLibraryScreen
import com.example.gametrack.ui.theme.GameTrackTheme
import com.example.gametrack.viewmodel.GameViewModel
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = GameRepository(database.gameDao())

        setContent {
            GameTrackTheme {
                val gameViewModel: GameViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(
                            modelClass: Class<T>
                        ): T {
                            return GameViewModel(repository) as T
                        }
                    }
                )

                val games by gameViewModel.allGames
                    .collectAsStateWithLifecycle()

                var currentScreen by rememberSaveable {
                    mutableStateOf("library")
                }

                if (currentScreen == "library") {
                    GameLibraryScreen(
                        games = games,
                        onAddGame = {
                            currentScreen = "add"
                        }
                    )
                } else {
                    AddGameScreen(
                        onSaveGame = { title, genre, platform, status ->
                            gameViewModel.addGame(
                                title,
                                genre,
                                platform,
                                status
                            )
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
