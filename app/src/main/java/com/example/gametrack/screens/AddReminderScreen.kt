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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gametrack.R
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

    val gameNameError = stringResource(R.string.enter_game_name)
    val eventNameError = stringResource(R.string.enter_event_name)
    val futureDateError = stringResource(R.string.select_future_date)

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
                stringResource(R.string.create_reminder)
            } else {
                stringResource(R.string.edit_reminder)
            },
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = gameTitle,
            onValueChange = { gameTitle = it },
            label = {
                Text(stringResource(R.string.game_name))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = {
                Text(stringResource(R.string.event_or_reward))
            },
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
            Text(
                stringResource(
                    R.string.date_details,
                    dateFormat.format(calendar.time)
                )
            )
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
            Text(
                stringResource(
                    R.string.time_details,
                    timeFormat.format(calendar.time)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.repeat_reminder),
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

                Text(
                    when (option) {
                        "Never" -> stringResource(R.string.repeat_never)
                        "Daily" -> stringResource(R.string.repeat_daily)
                        "Weekly" -> stringResource(R.string.repeat_weekly)
                        else -> option
                    }
                )
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
                        errorMessage = gameNameError
                    }

                    eventName.isBlank() -> {
                        errorMessage = eventNameError
                    }

                    reminderTime <= System.currentTimeMillis() -> {
                        errorMessage = futureDateError
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
                    stringResource(R.string.save_reminder)
                } else {
                    stringResource(R.string.update_reminder)
                }
            )
        }

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.cancel))
        }
    }
}