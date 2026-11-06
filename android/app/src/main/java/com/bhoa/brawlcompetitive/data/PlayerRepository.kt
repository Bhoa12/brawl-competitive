package com.bhoa.brawlcompetitive.data

import com.bhoa.brawlcompetitive.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL

data class PlayerProfile(
    val name: String,
    val tag: String,
    val trophies: Int,
    val highestTrophies: Int,
    val experienceLevel: Int,
    val iconId: Int?
)

object PlayerRepository {
    suspend fun load(tag: String): Result<PlayerProfile> = withContext(Dispatchers.IO) {
        val clean = tag.trim().removePrefix("#")
        if (clean.isBlank()) return@withContext Result.failure(IllegalArgumentException("Introduce un tag de jugador"))
        val base = BuildConfig.COMPETITIVE_API_BASE_URL.trim().removeSuffix("/")
        if (!base.startsWith("http")) return@withContext Result.failure(IllegalStateException("Backend no configurado"))
        var connection: HttpURLConnection? = null
        try {
            val encoded = URLEncoder.encode(clean, "UTF-8")
            connection = URL("$base/players/$encoded").openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 10000
            connection.setRequestProperty("Accept", "application/json")
            if (connection.responseCode !in 200..299) {
                return@withContext Result.failure(IllegalStateException("No se encontró el jugador o el servicio no está disponible"))
            }
            val root = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
            Result.success(
                PlayerProfile(
                    name = root.optString("name"),
                    tag = root.optString("tag"),
                    trophies = root.optInt("trophies"),
                    highestTrophies = root.optInt("highestTrophies"),
                    experienceLevel = root.optInt("expLevel"),
                    iconId = root.optJSONObject("icon")?.optInt("id")
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            connection?.disconnect()
        }
    }
}
