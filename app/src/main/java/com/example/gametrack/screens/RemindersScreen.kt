package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.gametrack.R
import com.example.gametrack.data.Reminder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RemindersScreen(
    reminders: List<Reminder>,
    onAddReminder: () -> Unit,
    onEditReminder: (Reminder) -> Unit,
    onDeleteReminder: (Reminder) -> Unit,
    onBack: () -> Unit
) {
    var reminderToDelete by remember {
        mutableStateOf<Reminder?>(null)
    }

    val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.game_reminders),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddReminder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.create_reminder))
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back_to_library))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (reminders.isEmpty()) {
            Text(stringResource(R.string.no_reminders))
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reminders, key = { it.reminderID }) { reminder ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = reminder.gameTitle,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(reminder.eventName)

                        Text(
                            stringResource(
                                R.string.scheduled_details,
                                dateFormat.format(
                                    Date(reminder.reminderTime)
                                )
                            )
                        )

                        val translatedRepeat = when (reminder.repeatType) {
                            "Never" -> stringResource(R.string.repeat_never)
                            "Daily" -> stringResource(R.string.repeat_daily)
                            "Weekly" -> stringResource(R.string.repeat_weekly)
                            else -> reminder.repeatType
                        }

                        Text(
                            stringResource(
                                R.string.repeat_details,
                                translatedRepeat
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onEditReminder(reminder)
                                }
                            ) {
                                Text(stringResource(R.string.edit))
                            }

                            OutlinedButton(
                                onClick = {
                                    reminderToDelete = reminder
                                }
                            ) {
                                Text(stringResource(R.string.delete))
                            }
                        }
                    }
                }
            }
        }
    }

    reminderToDelete?.let { reminder ->
        AlertDialog(
            onDismissRequest = {
                reminderToDelete = null
            },
            title = {
                Text(stringResource(R.string.delete_reminder))
            },
            text = {
                Text(stringResource(R.string.delete_reminder_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteReminder(reminder)
                        reminderToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        reminderToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}