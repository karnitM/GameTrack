package com.example.gametrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gametrack.data.AppDatabase
import com.example.gametrack.data.Game
import com.example.gametrack.data.Reminder
import com.example.gametrack.repository.GameRepository
import com.example.gametrack.repository.ReminderRepository
import com.example.gametrack.notifications.ReminderScheduler
import com.example.gametrack.screens.*
import com.example.gametrack.ui.theme.GameTrackTheme
import com.example.gametrack.viewmodel.GameViewModel
import com.example.gametrack.viewmodel.ReminderViewModel

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        val repository = GameRepository(database.gameDao())

        val reminderRepository =
            ReminderRepository(database.reminderDao())

        val reminderScheduler =
            ReminderScheduler(applicationContext)

        setContent {
            GameTrackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {

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

                    val reminderViewModel: ReminderViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {
                                return ReminderViewModel(
                                    reminderRepository,
                                    reminderScheduler
                                ) as T
                            }
                        }
                    )

                    val games by gameViewModel.allGames
                        .collectAsStateWithLifecycle()

                    val reminders by reminderViewModel.allReminders
                        .collectAsStateWithLifecycle()

                    var currentScreen by rememberSaveable {
                        mutableStateOf("library")
                    }

                    var selectedGame by remember {
                        mutableStateOf<Game?>(null)
                    }

                    var selectedReminder by remember {
                        mutableStateOf<Reminder?>(null)
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
                                },
                                onStatistics = {
                                    currentScreen = "statistics"
                                },
                                onSearchGames = {
                                    currentScreen = "search"
                                },
                                onReminders = {
                                    currentScreen = "reminders"
                                    requestNotificationPermission()
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

                        "statistics" -> {
                            StatisticsScreen(
                                games = games,
                                onBack = {
                                    currentScreen = "library"
                                }
                            )
                        }

                        "search" -> {
                            SearchGamesScreen(
                                onBack = {
                                    currentScreen = "library"
                                },
                                onSaveGame = { title, genre, platform, status ->
                                    gameViewModel.addGame(
                                        title,
                                        genre,
                                        platform,
                                        status
                                    )
                                }
                            )
                        }

                        "reminders" -> {
                            RemindersScreen(
                                reminders = reminders,
                                onAddReminder = {
                                    currentScreen = "addReminder"
                                },
                                onEditReminder = { reminder ->
                                    selectedReminder = reminder
                                    currentScreen = "editReminder"
                                },
                                onDeleteReminder = { reminder ->
                                    reminderViewModel.deleteReminder(reminder)
                                },
                                onBack = {
                                    currentScreen = "library"
                                }
                            )
                        }

                        "addReminder" -> {
                            AddReminderScreen(
                                onSaveReminder = {
                                        gameTitle, eventName, reminderTime, repeatType ->

                                    reminderViewModel.addReminder(
                                        gameTitle,
                                        eventName,
                                        reminderTime,
                                        repeatType
                                    )

                                    currentScreen = "reminders"
                                },
                                onCancel = {
                                    currentScreen = "reminders"
                                }
                            )
                        }

                        "editReminder" -> {
                            selectedReminder?.let { reminder ->
                                AddReminderScreen(
                                    reminder = reminder,
                                    onSaveReminder = {
                                            gameTitle, eventName, reminderTime, repeatType ->

                                        val updatedReminder = reminder.copy(
                                            gameTitle = gameTitle,
                                            eventName = eventName,
                                            reminderTime = reminderTime,
                                            repeatType = repeatType
                                        )

                                        reminderViewModel.updateReminder(
                                            updatedReminder
                                        )

                                        selectedReminder = null
                                        currentScreen = "reminders"
                                    },
                                    onCancel = {
                                        selectedReminder = null
                                        currentScreen = "reminders"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}