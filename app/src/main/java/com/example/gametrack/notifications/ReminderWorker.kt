package com.example.gametrack.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gametrack.R

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val gameTitle = inputData.getString("gameTitle")
            ?: return Result.failure()

        val eventName = inputData.getString("eventName")
            ?: return Result.failure()

        val reminderID = inputData.getInt("reminderID", 0)

        val channel = NotificationChannel(
            "game_reminders",
            applicationContext.getString(R.string.game_reminders),
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.createNotificationChannel(channel)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            "game_reminders"
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(gameTitle)
            .setContentText(eventName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(reminderID, notification)

        return Result.success()
    }
}
