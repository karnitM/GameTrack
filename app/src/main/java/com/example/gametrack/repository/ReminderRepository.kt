package com.example.gametrack.repository

import com.example.gametrack.data.Reminder
import com.example.gametrack.data.ReminderDao

class ReminderRepository(private val reminderDao: ReminderDao) {

    val allReminders = reminderDao.getAllReminders()

    suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    suspend fun getReminderById(id: Int): Reminder? {
        return reminderDao.getReminderById(id)
    }
}
