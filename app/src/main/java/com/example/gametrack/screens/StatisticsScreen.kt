package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            text = "Game Statistics",
            style = MaterialTheme.typography.headlineMedium
        )

        StatisticCard("Total Games", totalGames)
        StatisticCard("Completed Games", completedGames)
        StatisticCard("Currently Playing", currentlyPlaying)
        StatisticCard("Want to Play", wantToPlay)

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Library")
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
