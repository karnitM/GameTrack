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

@Composable
fun GameLibraryScreen(
    games: List<Game>,
    onAddGame: () -> Unit,
    onGameClick: (Game) -> Unit
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
            text = "My Game Library",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search games") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddGame,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Game")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredGames.isEmpty()) {
            Text(
                text = if (searchText.isBlank()) {
                    "No games saved yet. Add your first game!"
                } else {
                    "No games found."
                }
            )
        } else {
            LazyColumn(
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
                                text = game.status,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
