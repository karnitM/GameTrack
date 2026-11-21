package com.example.gametrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val gameID: Int = 0,
    val title: String,
    val genre: String,
    val platform: String,
    val status: String
)
