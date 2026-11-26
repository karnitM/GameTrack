package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gametrack.R
import com.example.gametrack.data.Game

@Composable
fun GameDetailsScreen(
    game: Game,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = game.title,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        Text(stringResource(R.string.genre_details, game.genre))
        Text(stringResource(R.string.platform_details, game.platform))
        val translatedStatus = when (game.status) {
            "Want to Play" -> stringResource(R.string.status_want_to_play)
            "Currently Playing" -> stringResource(R.string.status_playing)
            "Completed" -> stringResource(R.string.status_completed)
            else -> game.status
        }

        Text(stringResource(R.string.status_details, translatedStatus))
        Button(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.edit_game))
        }

        Button(
            onClick = {
                showDeleteDialog = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(stringResource(R.string.delete_game))
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back_to_library))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(stringResource(R.string.delete_game_title))
            },
            text = {
                Text(stringResource(R.string.delete_game_message, game.title))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
