package com.example.gametrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val reminderID: Int = 0,
    val gameTitle: String,
    val eventName: String,
    val reminderTime: Long,
    val repeatType: String = "Never"
)
