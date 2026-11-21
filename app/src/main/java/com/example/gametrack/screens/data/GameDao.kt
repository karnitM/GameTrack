package com.example.gametrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert
    suspend fun insertGame(game: Game)

    @Update
    suspend fun updateGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("SELECT * FROM games ORDER BY title ASC")
    fun getAllGames(): Flow<List<Game>>

    @Query("SELECT * FROM games WHERE gameID = :id")
    suspend fun getGameById(id: Int): Game?
}
