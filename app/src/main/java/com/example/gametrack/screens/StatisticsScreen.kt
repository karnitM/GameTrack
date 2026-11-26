package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.gametrack.R
import com.example.gametrack.data.Game

@Composable
fun StatisticsScreen(
    games: List<Game>,
    onBack: () -> Unit
) {
    val totalGames = games.size
    val completedGames = games.count {
        it.status == "Completed"
    }
    val currentlyPlaying = games.count {
        it.status == "Currently Playing"
    }
    val wantToPlay = games.count {
        it.status == "Want to Play"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.game_statistics),
            style = MaterialTheme.typography.headlineMedium
        )

        StatisticCard(
            stringResource(R.string.total_games),
            totalGames
        )

        StatisticCard(
            stringResource(R.string.completed_games),
            completedGames
        )

        StatisticCard(
            stringResource(R.string.status_playing),
            currentlyPlaying
        )

        StatisticCard(
            stringResource(R.string.status_want_to_play),
            wantToPlay
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back_to_library))
        }
    }
}

@Composable
fun StatisticCard(
    title: String,
    count: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}