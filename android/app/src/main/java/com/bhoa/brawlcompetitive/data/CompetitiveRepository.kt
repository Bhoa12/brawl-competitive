package com.bhoa.brawlcompetitive.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object CompetitiveRepository {
    /*
     * Endpoint configurable para un backend propio.
     * La APK nunca debe inventar estadísticas: si no existe una fuente válida,
     * devuelve una lista vacía y la UI lo comunica.
     */
    private const val META_URL = "https://example.invalid/brawl/meta"

    suspend fun loadMeta(): List<MetaEntry> = withContext(Dispatchers.IO) {
        try {
            val connection = URL(META_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.use {
                if (it.responseCode !in 200..299) return@withContext emptyList()
                val root = JSONObject(it.inputStream.bufferedReader().use { reader -> reader.readText() })
                parse(root.optJSONArray("entries") ?: JSONArray())
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parse(array: JSONArray) = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            add(MetaEntry(
                brawlerId = item.optInt("brawlerId"),
                brawlerName = item.optString("brawlerName"),
                mapName = item.optString("mapName").takeIf { it.isNotBlank() },
                mode = item.optString("mode").takeIf { it.isNotBlank() },
                rank = item.optString("rank", "General"),
                subRank = item.optString("subRank").takeIf { it.isNotBlank() },
                pickRate = item.optDouble("pickRate").takeIf { !it.isNaN() },
                winRate = item.optDouble("winRate").takeIf { !it.isNaN() },
                sampleSize = item.optInt("sampleSize").takeIf { it > 0 },
                updatedAt = item.optLong("updatedAt").takeIf { it > 0 }
            ))
        }
    }
}