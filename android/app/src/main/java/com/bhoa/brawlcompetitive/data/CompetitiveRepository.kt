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
        get() {
            val base = BuildConfig.COMPETITIVE_API_BASE_URL.trim().removeSuffix("/")
            return if (base.startsWith("http")) "$base/meta/snapshot" else DEFAULT_META_URL
        }

    suspend fun loadMetaDocument(): MetaDocument? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            connection = URL(metaUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 10000
            connection.setRequestProperty("Accept", "application/json")
            if (connection.responseCode !in 200..299) return@withContext null

            val json = connection.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(json)
            val entries = root.optJSONArray("entries")
                ?: root.optJSONArray("metrics")
                ?: JSONArray()
            MetaDocument(
                entries = parse(entries),
                generatedAt = root.optString("generatedAt")
                    .takeIf { it.isNotBlank() }
                    ?: root.optString("updated_at").takeIf { it.isNotBlank() },
                totalMatches = root.optInt("totalMatches")
                    .takeIf { it > 0 }
                    ?: root.optInt("sample_size").takeIf { it > 0 },
                methodology = root.optString("methodology").takeIf { it.isNotBlank() }
            )
        } catch (_: Exception) {
            null
        } finally {
            connection?.disconnect()
        }
    }

    suspend fun loadMeta(): List<MetaEntry> = loadMetaDocument()?.entries ?: emptyList()

    private fun backendUrl(path: String): String? {
        val base = BuildConfig.COMPETITIVE_API_BASE_URL.trim().removeSuffix("/")
        return base.takeIf { it.startsWith("http") }?.plus(path)
    }

    suspend fun loadCatalogBrawlers(): List<Brawler> = loadCatalogArray("/catalog/brawlers") { item ->
        val image = item.optJSONObject("image")?.optString("url")?.takeIf { it.isNotBlank() }
        Brawler(id = item.optString("id"), name = item.optString("name"), imageUrl = image, description = item.optString("description").takeIf { it.isNotBlank() }, brawlerClass = item.optString("class").takeIf { it.isNotBlank() }, rarity = item.optString("rarity").takeIf { it.isNotBlank() }, gadgets = parseVisualAssets(item.optJSONArray("gadgets")), starPowers = parseVisualAssets(item.optJSONArray("starPowers")), gears = parseVisualAssets(item.optJSONArray("gears")), buffies = parseVisualAssets(item.optJSONArray("buffies")))
    }

    suspend fun loadCompetitiveMaps(): List<BrawlMap> = loadCatalogArray("/catalog/maps") { item ->
        val image = item.optJSONObject("image")?.optString("url")?.takeIf { it.isNotBlank() }
        BrawlMap(id = item.optString("id").toIntOrNull() ?: 0, name = item.optString("name"), mode = item.optString("mode"), imageUrl = image)
    }

    private suspend fun <T> loadCatalogArray(path: String, mapper: (JSONObject) -> T): List<T> = withContext(Dispatchers.IO) {
        val url = backendUrl(path) ?: return@withContext emptyList()
        var connection: HttpURLConnection? = null
        try {
            connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 10000
            connection.setRequestProperty("Accept", "application/json")
            if (connection.responseCode !in 200..299) return@withContext emptyList()
            val array = JSONArray(connection.inputStream.bufferedReader().use { it.readText() })
            buildList {
                for (i in 0 until array.length()) array.optJSONObject(i)?.let { add(mapper(it)) }
            }
        } catch (_: Exception) {
            emptyList()
        } finally {
            connection?.disconnect()
        }
    }


    private fun parseVisualAssets(array: JSONArray?): List<VisualAsset> = buildList {
        if (array == null) return@buildList
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val image = item.optJSONObject("image")?.optString("url")?.takeIf { it.isNotBlank() } ?: item.optString("imageUrl").takeIf { it.isNotBlank() }
            add(VisualAsset(item.optString("id"), item.optString("name"), image, item.optString("description").takeIf { it.isNotBlank() }))
        }
    }
    private fun parse(array: JSONArray): List<MetaEntry> = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            add(
                MetaEntry(
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
                )
            )
        }
    }

    private fun JSONObject.takeNullableDouble(name: String): Double? =
        if (isNull(name)) null else optDouble(name).takeIf { !it.isNaN() }
}
