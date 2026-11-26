package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import com.example.gametrack.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGameScreen(
    onSaveGame: (String, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var platform by rememberSaveable { mutableStateOf("") }
    var status by rememberSaveable { mutableStateOf("Want to Play") }
    var statusExpanded by remember { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.add_new_game),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics {
                heading()
            }
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = {
                Text(stringResource(R.string.game_title))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && title.isBlank()
        )

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = {
                Text(stringResource(R.string.genre))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && genre.isBlank()
        )

        OutlinedTextField(
            value = platform,
            onValueChange = { platform = it },
            label = {
                Text(stringResource(R.string.platform))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && platform.isBlank()
        )

        ExposedDropdownMenuBox(
            expanded = statusExpanded,
            onExpandedChange = {
                statusExpanded = !statusExpanded
            }
        ) {
            OutlinedTextField(
                value = when (status) {
                    "Want to Play" ->
                        stringResource(R.string.status_want_to_play)

                    "Currently Playing" ->
                        stringResource(R.string.status_playing)

                    "Completed" ->
                        stringResource(R.string.status_completed)

                    else -> status
                },
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(stringResource(R.string.playing_status))
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = statusExpanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        ExposedDropdownMenuAnchorType.PrimaryNotEditable
                    )
            )

            ExposedDropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = {
                    statusExpanded = false
                }
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
                                    "Want to Play" ->
                                        stringResource(R.string.status_want_to_play)

                                    "Currently Playing" ->
                                        stringResource(R.string.status_playing)

                                    "Completed" ->
                                        stringResource(R.string.status_completed)

                                    else -> option
                                }
                            )
                        },
                        onClick = {
                            status = option
                            statusExpanded = false
                        }
                    )
                }
            }
        }

        if (
            showError &&
            (title.isBlank() || genre.isBlank() || platform.isBlank())
        ) {
            Text(
                text = stringResource(R.string.fill_all_fields),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                }
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
                    showError = false

                    onSaveGame(
                        title.trim(),
                        genre.trim(),
                        platform.trim(),
                        status
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
        ) {
            Text(stringResource(R.string.save_game))
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
        ) {
            Text(stringResource(R.string.cancel))
        }
    }
}