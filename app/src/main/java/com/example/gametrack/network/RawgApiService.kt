package com.example.gametrack.network

import retrofit2.http.GET
import retrofit2.http.Query

data class RawgGenre(
    val name: String
)

data class RawgPlatform(
    val name: String
)

data class RawgPlatformEntry(
    val platform: RawgPlatform
)

data class RawgGame(
    val id: Int,
    val name: String,
    val background_image: String?,
    val released: String?,
    val genres: List<RawgGenre>?,
    val platforms: List<RawgPlatformEntry>?
)

data class RawgGameResponse(
    val results: List<RawgGame>
)

interface RawgApiService {

    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String,
        @Query("search") search: String,
        @Query("page_size") pageSize: Int = 20
    ): RawgGameResponse
}
