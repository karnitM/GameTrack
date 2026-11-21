package com.example.gametrack.repository

import com.example.gametrack.data.Game
import com.example.gametrack.data.GameDao
import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameDao: GameDao) {

    val allGames: Flow<List<Game>> = gameDao.getAllGames()

    suspend fun insertGame(game: Game) {
        gameDao.insertGame(game)
    }

    suspend fun updateGame(game: Game) {
        gameDao.updateGame(game)
    }

    suspend fun deleteGame(game: Game) {
        gameDao.deleteGame(game)
    }

    suspend fun getGameById(id: Int): Game? {
        return gameDao.getGameById(id)
    }
}
