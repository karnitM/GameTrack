package com.example.gametrack.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gametrack.data.Reminder
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddReminderScreen(
    onSaveReminder: (String, String, Long, String) -> Unit,
    onCancel: () -> Unit,
    reminder: Reminder? = null
) {
    val context = LocalContext.current

    var gameTitle by remember {
        mutableStateOf(reminder?.gameTitle ?: "")
    }

    var eventName by remember {
        mutableStateOf(reminder?.eventName ?: "")
    }

    var repeatType by remember {
        mutableStateOf(reminder?.repeatType ?: "Never")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var reminderTime by remember {
        mutableStateOf(
            reminder?.reminderTime
                ?: (System.currentTimeMillis() + 60000)
        )
    }

    val calendar = Calendar.getInstance().apply {
        timeInMillis = reminderTime
    }

    val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    val timeFormat = SimpleDateFormat(
        "HH:mm",
        Locale.getDefault()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = if (reminder == null) {
                "Create Reminder"
            } else {
                "Edit Reminder"
            },
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = gameTitle,
            onValueChange = { gameTitle = it },
            label = { Text("Game Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Event or Reward") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        calendar.set(year, month, day)
                        reminderTime = calendar.timeInMillis
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    datePicker.minDate = System.currentTimeMillis()
                }.show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Date: ${dateFormat.format(calendar.time)}")
        }

        OutlinedButton(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        reminderTime = calendar.timeInMillis
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Time: ${timeFormat.format(calendar.time)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Repeat Reminder",
            style = MaterialTheme.typography.titleMedium
        )

        listOf("Never", "Daily", "Weekly").forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = repeatType == option,
                    onClick = { repeatType = option }
                )

                Text(option)
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    gameTitle.isBlank() -> {
                        errorMessage = "Enter a game name."
                    }

                    eventName.isBlank() -> {
                        errorMessage = "Enter an event or reward."
                    }

                    reminderTime <= System.currentTimeMillis() -> {
                        errorMessage = "Select a future date and time."
                    }

                    else -> {
                        onSaveReminder(
                            gameTitle.trim(),
                            eventName.trim(),
                            reminderTime,
                            repeatType
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (reminder == null) {
                    "Save Reminder"
                } else {
                    "Update Reminder"
                }
            )
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
