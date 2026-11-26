
package com.example.gametrack.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gametrack.BuildConfig
import com.example.gametrack.network.RawgGame
import com.example.gametrack.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@Composable
fun SearchGamesScreen(
    onBack: () -> Unit,
    onSaveGame: (String, String, String, String) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }
    var games by remember { mutableStateOf<List<RawgGame>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf<RawgGame?>(null) }
    var selectedStatus by remember { mutableStateOf("Want to Play") }

    val scope = rememberCoroutineScope()

    selectedGame?.let { game ->
        AlertDialog(
            onDismissRequest = {
                selectedGame = null
            },
            title = {
                Text("Add ${game.name}")
            },
            text = {
                Column {
                    Text("Select your game status:")

                    val statuses = listOf(
                        "Want to Play",
                        "Currently Playing",
                        "Completed"
                    )

                    statuses.forEach { status ->
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatus == status,
                                onClick = {
                                    selectedStatus = status
                                }
                            )

                            Text(status)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val genre = game.genres
                            ?.joinToString(", ") { it.name }
                            ?.ifBlank { "Unknown" }
                            ?: "Unknown"

                        val platform = game.platforms
                            ?.joinToString(", ") { it.platform.name }
                            ?.ifBlank { "Unknown" }
                            ?: "Unknown"

                        onSaveGame(
                            game.name,
                            genre,
                            platform,
                            selectedStatus
                        )

                        selectedGame = null
                        onBack()
                    }
                ) {
                    Text("Save Game")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedGame = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search Online Games",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Enter game title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""
                    games = emptyList()

                    try {
                        val response = RetrofitClient.apiService.searchGames(
                            apiKey = BuildConfig.RAWG_API_KEY,
                            search = searchQuery.trim()
                        )

                        games = response.results

                        if (games.isEmpty()) {
                            errorMessage = "No games found."
                        }

                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        errorMessage = "Unable to load games. Please try again."
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = searchQuery.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Library")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(games) { game ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AsyncImage(
                            model = game.background_image,
                            contentDescription = "Cover image of ${game.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Released: ${game.released ?: "Unknown"}"
                        )

                        Text(
                            text = "Genres: ${game.genres?.joinToString(", ") { it.name }?.ifBlank { "Unknown" } ?: "Unknown"}"
                        )


                        Text(
                            text = "Platforms: ${game.platforms?.joinToString(", ") { it.platform.name }?.ifBlank { "Unknown" } ?: "Unknown"}"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                selectedGame = game
                                selectedStatus = "Want to Play"
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add to Library")
                        }

                    }
                }
            }
        }
    }
}
