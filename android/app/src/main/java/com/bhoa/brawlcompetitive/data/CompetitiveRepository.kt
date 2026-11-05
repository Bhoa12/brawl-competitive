package com.bhoa.brawlcompetitive.data

import com.bhoa.brawlcompetitive.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class MetaDocument(
    val entries: List<MetaEntry>,
    val generatedAt: String? = null,
    val totalMatches: Int? = null,
    val methodology: String? = null
)

object CompetitiveRepository {
    private const val DEFAULT_META_URL =
        "https://raw.githubusercontent.com/Bhoa12/brawl-competitive/main/backend/public/meta.json"

    private val metaUrl: String
        get() = BuildConfig.COMPETITIVE_API_BASE_URL
            .trim()
            .takeIf { it.isNotEmpty() }
            ?: DEFAULT_META_URL

    suspend fun loadMetaDocument(): MetaDocument? = withContext(Dispatchers.IO) {
        try {
            val connection = URL(metaUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 10000
            connection.setRequestProperty("Accept", "application/json")
            connection.use {
                if (it.responseCode !in 200..299) return@withContext null
                val root = JSONObject(it.inputStream.bufferedReader().use { reader -> reader.readText() })
                MetaDocument(
                    entries = parse(root.optJSONArray("entries") ?: JSONArray()),
                    generatedAt = root.optString("generatedAt").takeIf { value -> value.isNotBlank() },
                    totalMatches = root.optInt("totalMatches").takeIf { value -> value > 0 },
                    methodology = root.optString("methodology").takeIf { value -> value.isNotBlank() }
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun loadMeta(): List<MetaEntry> = loadMetaDocument()?.entries ?: emptyList()

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
                pickRate = item.takeNullableDouble("pickRate"),
                winRate = item.takeNullableDouble("winRate"),
                sampleSize = item.optInt("sampleSize").takeIf { it > 0 },
                updatedAt = item.optLong("updatedAt").takeIf { it > 0 }
            ))
        }
    }

    private fun JSONObject.takeNullableDouble(name: String): Double? =
        if (isNull(name)) null else optDouble(name).takeIf { !it.isNaN() }
}