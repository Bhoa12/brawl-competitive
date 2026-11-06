package com.bhoa.brawlcompetitive.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object MapRepository {
    // Pool competitivo actual. Se mantiene explícito para no mezclar mapas históricos
    // o de rotaciones casuales con la experiencia competitiva.
    private val competitiveMaps = setOf(
        "Double Swoosh", "Gem Fort", "Hard Rock Mine", "Undermine",
        "Bridge Too Far", "Hot Potato", "Kaboom Canyon", "Safe Zone",
        "Dry Season", "Hideout", "Layer Cake", "Shooting Star",
        "Beach Ball", "Center Stage", "Pinball Dreams", "Sneaky Fields",
        "Spiraling Out", "Triple Dribble",
        "Dueling Beetles", "Open Business", "Parallel Plays", "Ring of Fire",
        "Belle's Rock", "Flaring Phoenix", "New Horizons", "Out in the Open"
    )

    suspend fun loadMaps(): List<BrawlMap> = withContext(Dispatchers.IO) {
        CompetitiveRepository.loadCompetitiveMaps().takeIf { it.isNotEmpty() }?.let { return@withContext it }
        val connection = URL("https://api.brawlapi.com/v1/maps").openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 15000
        try {
            if (connection.responseCode !in 200..299) return@withContext emptyList()
            val raw = connection.inputStream.bufferedReader().use { it.readText() }
            parse(JSONObject(raw).optJSONArray("list") ?: JSONArray())
        } catch (_: Exception) {
            emptyList()
        } finally {
            connection.disconnect()
        }
    }

    private fun parse(array: JSONArray): List<BrawlMap> = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val name = item.optString("name")
            if (name !in competitiveMaps) continue
            val images = item.optJSONObject("imageUrl")
            val image = images?.optString("map")?.takeIf { it.isNotBlank() }
                ?: images?.optString("thumbnail")?.takeIf { it.isNotBlank() }
            add(
                BrawlMap(
                    id = item.optInt("id"),
                    name = name,
                    mode = item.optJSONObject("gameMode")?.optString("name"),
                    imageUrl = image,
                    environment = item.optJSONObject("environment")?.optString("name")
                )
            )
        }
    }
}
