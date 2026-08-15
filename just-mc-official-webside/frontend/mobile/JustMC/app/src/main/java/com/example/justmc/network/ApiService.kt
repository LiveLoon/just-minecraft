package com.example.justmc.network

import com.example.justmc.data.*
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("player/list")
    suspend fun getPlayerList(): List<Player>

    @GET("stats")
    suspend fun getStats(): StatsResponse

    @GET("top/{statKey}")
    suspend fun getTop(@Path("statKey") statKey: String): TopResponse

    @GET("backup/list")
    suspend fun getBackupList(): List<BackupFile>
}