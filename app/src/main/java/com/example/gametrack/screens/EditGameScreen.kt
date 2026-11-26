package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.gametrack.R
import com.example.gametrack.data.Game

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGameScreen(
    game: Game,
    onSave: (Game) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember(game.gameID) {
        mutableStateOf(game.title)
    }
    var genre by remember(game.gameID) {
        mutableStateOf(game.genre)
    }
    var platform by remember(game.gameID) {
        mutableStateOf(game.platform)
    }
    var status by remember(game.gameID) {
        mutableStateOf(game.status)
    }
    var expanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.edit_game),
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.game_title)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text(stringResource(R.string.genre)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = platform,
            onValueChange = { platform = it },
            label = { Text(stringResource(R.string.platform)) },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = when (status) {
                    "Want to Play" -> stringResource(R.string.status_want_to_play)
                    "Currently Playing" -> stringResource(R.string.status_playing)
                    "Completed" -> stringResource(R.string.status_completed)
                    else -> status
                },
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.playing_status)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        ExposedDropdownMenuAnchorType.PrimaryNotEditable
                    )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf(
                    "Want to Play",
                    "Currently Playing",
                    "Completed"
                ).forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                when (option) {
                                    "Want to Play" -> stringResource(R.string.status_want_to_play)
                                    "Currently Playing" -> stringResource(R.string.status_playing)
                                    "Completed" -> stringResource(R.string.status_completed)
                                    else -> option
                                }
                            )
                        },
                        onClick = {
                            status = option
                            expanded = false
                        }
                    )
                }
            }
        }

        if (showError) {
            Text(
                text = stringResource(R.string.fill_all_fields),
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                if (
                    title.isBlank() ||
                    genre.isBlank() ||
                    platform.isBlank()
                ) {
                    showError = true
                } else {
                    onSave(
                        game.copy(
                            title = title.trim(),
                            genre = genre.trim(),
                            platform = platform.trim(),
                            status = status
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_changes))
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.cancel))
        }
    }
}