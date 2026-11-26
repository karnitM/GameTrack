package com.example.gametrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gametrack.data.Reminder
import com.example.gametrack.repository.ReminderRepository
import com.example.gametrack.notifications.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: ReminderRepository,
    private val scheduler: ReminderScheduler
) : ViewModel() {

    val allReminders = repository.allReminders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addReminder(
        gameTitle: String,
        eventName: String,
        reminderTime: Long,
        repeatType: String
    ) {
        viewModelScope.launch {

            val reminder = Reminder(
                gameTitle = gameTitle,
                eventName = eventName,
                reminderTime = reminderTime,
                repeatType = repeatType
            )

            val id = repository.insertReminder(reminder)

            scheduler.scheduleReminder(
                reminder.copy(reminderID = id.toInt())
            )
        }
    }

    fun updateReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder)
            scheduler.scheduleReminder(reminder)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            scheduler.cancelReminder(reminder.reminderID)
            repository.deleteReminder(reminder)
        }
    }
}
