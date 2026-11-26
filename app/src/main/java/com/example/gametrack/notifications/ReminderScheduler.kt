package com.example.gametrack.notifications

import android.content.Context
import androidx.work.*
import com.example.gametrack.data.Reminder
import java.util.concurrent.TimeUnit

class ReminderScheduler(context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleReminder(reminder: Reminder) {

        val delay = (reminder.reminderTime -
                System.currentTimeMillis()).coerceAtLeast(0L)

        val data = workDataOf(
            "reminderID" to reminder.reminderID,
            "gameTitle" to reminder.gameTitle,
            "eventName" to reminder.eventName
        )

        val workName = "reminder_${reminder.reminderID}"

        workManager.cancelUniqueWork(workName)

        if (reminder.repeatType == "Daily" ||
            reminder.repeatType == "Weekly"
        ) {

            val interval = if (reminder.repeatType == "Daily") {
                1L
            } else {
                7L
            }

            val request =
                PeriodicWorkRequestBuilder<ReminderWorker>(
                    interval,
                    TimeUnit.DAYS
                )
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                workName,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                request
            )

        } else {

            val request =
                OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build()

            workManager.enqueueUniqueWork(
                workName,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }

    fun cancelReminder(reminderID: Int) {
        workManager.cancelUniqueWork("reminder_$reminderID")
    }
}
