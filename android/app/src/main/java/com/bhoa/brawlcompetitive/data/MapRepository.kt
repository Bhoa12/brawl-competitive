package com.bhoa.brawlcompetitive.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object MapRepository {
    suspend fun loadMaps(): List<BrawlMap> = withContext(Dispatchers.IO) {
        val connection = URL("https://api.brawlapi.com/v1/maps").openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 15000
        try {
            if (connection.responseCode !in 200..299) return@withContext emptyList()
            val raw = connection.inputStream.bufferedReader().use { it.readText() }
            parse(JSONObject(raw).optJSONArray("list") ?: JSONArray())
        } catch (_: Exception) { emptyList() } finally { connection.disconnect() }
    }

    private fun parse(array: JSONArray) = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val images = item.optJSONObject("imageUrl")
            val image = images?.optString("map")?.takeIf { it.isNotBlank() }
                ?: images?.optString("thumbnail")?.takeIf { it.isNotBlank() }
            add(BrawlMap(
                id = item.optInt("id"),
                name = item.optString("name"),
                mode = item.optJSONObject("gameMode")?.optString("name"),
                imageUrl = image,
                environment = item.optJSONObject("environment")?.optString("name")
            ))
        }
    }
}