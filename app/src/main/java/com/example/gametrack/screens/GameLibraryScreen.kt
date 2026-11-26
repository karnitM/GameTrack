package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import com.example.gametrack.data.Game
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
    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    val filteredGames = games.filter {
        it.title.contains(searchText, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.my_game_library),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    heading()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = {
                    Text(stringResource(R.string.search_my_library))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Button(
                onClick = onAddGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_game),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(stringResource(R.string.add_game_manually))
            }
        }

        item {
            OutlinedButton(
                onClick = onStatistics,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_statistics),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(stringResource(R.string.view_statistics))
            }
        }

        item {
            Button(
                onClick = onSearchGames,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_games),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(stringResource(R.string.discover_games_online))
            }
        }

        item {
            OutlinedButton(
                onClick = onReminders,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reminders),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(stringResource(R.string.game_reminders))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (filteredGames.isEmpty()) {
            item {
                Text(
                    text = if (searchText.isBlank()) {
                        stringResource(R.string.no_games_saved)
                    } else {
                        stringResource(R.string.no_games_found)
                    },
                    modifier = Modifier.semantics {
                        liveRegion = LiveRegionMode.Polite
                    }
                )
            }
        } else {
            items(
                items = filteredGames,
                key = { it.gameID }
            ) { game ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable(role = Role.Button) {
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
                                "Want to Play" ->
                                    stringResource(R.string.status_want_to_play)

                                "Currently Playing" ->
                                    stringResource(R.string.status_playing)

                                "Completed" ->
                                    stringResource(R.string.status_completed)

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