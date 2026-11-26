package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            text = "Game Reminders",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddReminder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Reminder")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Library")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (reminders.isEmpty()) {
            Text("No reminders created yet.")
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
                            "Scheduled: ${
                                dateFormat.format(
                                    Date(reminder.reminderTime)
                                )
                            }"
                        )

                        Text("Repeat: ${reminder.repeatType}")

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
                                Text("Edit")
                            }

                            OutlinedButton(
                                onClick = {
                                    reminderToDelete = reminder
                                }
                            ) {
                                Text("Delete")
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
                Text("Delete Reminder")
            },
            text = {
                Text("Are you sure you want to delete this reminder?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteReminder(reminder)
                        reminderToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        reminderToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
