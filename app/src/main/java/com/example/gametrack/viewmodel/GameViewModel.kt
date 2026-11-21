package com.example.gametrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gametrack.data.Game
import com.example.gametrack.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameRepository
) : ViewModel() {

    val allGames = repository.allGames.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addGame(
        title: String,
        genre: String,
        platform: String,
        status: String
    ) {
        viewModelScope.launch {
            repository.insertGame(
                Game(
                    title = title,
                    genre = genre,
                    platform = platform,
                    status = status
                )
            )
        }
    }

    fun updateGame(game: Game) {
        viewModelScope.launch {
            repository.updateGame(game)
        }
    }

    fun deleteGame(game: Game) {
        viewModelScope.launch {
            repository.deleteGame(game)
        }
    }
}
