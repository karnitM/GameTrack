package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gametrack.data.Game
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.stringResource
import com.example.gametrack.R

@Composable
fun GameLibraryScreen(
    games: List<Game>,
    onAddGame: () -> Unit,
    onGameClick: (Game) -> Unit,
    onStatistics: () -> Unit,
    onSearchGames: () -> Unit,
    onReminders: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    val filteredGames = games.filter {
        it.title.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.my_game_library),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(stringResource(R.string.search_my_library)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddGame,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_game_manually))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onStatistics,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.view_statistics))
        }

        Button(
            onClick = onSearchGames,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.discover_games_online))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onReminders,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.game_reminders))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredGames.isEmpty()) {
            Text(
                text = if (searchText.isBlank()) {
                    stringResource(R.string.no_games_saved)
                } else {
                    stringResource(R.string.no_games_found)
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = filteredGames,
                    key = { it.gameID }
                ) { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onGameClick(game)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = game.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = game.genre + " • " + game.platform,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = when (game.status) {
                                    "Want to Play" -> stringResource(R.string.status_want_to_play)
                                    "Currently Playing" -> stringResource(R.string.status_playing)
                                    "Completed" -> stringResource(R.string.status_completed)
                                    else -> game.status
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
