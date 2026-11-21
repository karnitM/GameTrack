package com.example.gametrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gametrack.data.AppDatabase
import com.example.gametrack.data.Game
import com.example.gametrack.repository.GameRepository
import com.example.gametrack.screens.AddGameScreen
import com.example.gametrack.screens.GameDetailsScreen
import com.example.gametrack.screens.GameLibraryScreen
import com.example.gametrack.ui.theme.GameTrackTheme
import com.example.gametrack.viewmodel.GameViewModel
import com.example.gametrack.screens.EditGameScreen

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

                val games by gameViewModel.allGames.collectAsStateWithLifecycle()

                var currentScreen by rememberSaveable {
                    mutableStateOf("library")
                }

                var selectedGame by remember {
                    mutableStateOf<Game?>(null)
                }

                when (currentScreen) {
                    "library" -> {
                        GameLibraryScreen(
                            games = games,
                            onAddGame = {
                                currentScreen = "add"
                            },
                            onGameClick = { game ->
                                selectedGame = game
                                currentScreen = "details"
                            }
                        )
                    }

                    "add" -> {
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

                    "details" -> {
                        selectedGame?.let { game ->
                            GameDetailsScreen(
                                game = game,
                                onEdit = {
                                    currentScreen = "edit"
                                },
                                onDelete = {
                                    gameViewModel.deleteGame(game)
                                    selectedGame = null
                                    currentScreen = "library"
                                },
                                onBack = {
                                    selectedGame = null
                                    currentScreen = "library"
                                }
                            )
                        }
                    }

                    "edit" -> {
                        selectedGame?.let { game ->
                            EditGameScreen(
                                game = game,
                                onSave = { updatedGame ->
                                    gameViewModel.updateGame(updatedGame)
                                    selectedGame = updatedGame
                                    currentScreen = "details"
                                },
                                onCancel = {
                                    currentScreen = "details"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
