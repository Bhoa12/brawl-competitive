package com.bhoa.brawlcompetitive.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object BrawlRepository {
    suspend fun loadBrawlers(): List<Brawler> = withContext(Dispatchers.IO) {
        CompetitiveRepository.loadCatalogBrawlers().takeIf { it.isNotEmpty() }?.let { return@withContext it }
        var connection: HttpURLConnection? = null
        try {
            connection = URL("https://api.brawlapi.com/v1/brawlers").openConnection() as HttpURLConnection
            connection.connectTimeout = 10000; connection.readTimeout = 15000
            if (connection.responseCode !in 200..299) return@withContext emptyList()
            val raw = connection.inputStream.bufferedReader().use { it.readText() }
            parse(JSONObject(raw).optJSONArray("list") ?: JSONArray())
        } catch (_: Exception) { emptyList() } finally { connection?.disconnect() }
    }

    private fun parse(array: JSONArray): List<Brawler> = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val name = item.optString("name")
            if (name.equals("Buzz Lightyear", true)) continue
            add(Brawler(
                id=item.optString("id"), name=name, imageUrl=extractImageUrl(item),
                description=item.optString("description").takeIf { it.isNotBlank() },
                brawlerClass=item.optJSONObject("class")?.optString("name"),
                rarity=item.optJSONObject("rarity")?.optString("name"),
                gadgets=parseAssets(item.optJSONArray("gadgets")),
                starPowers=parseAssets(item.optJSONArray("starPowers")),
                gears=parseAssets(item.optJSONArray("gears") ?: item.optJSONArray("gear")),
                buffies=parseAssets(item.optJSONArray("buffies"))
            ))
        }
    }
    private fun parseAssets(array: JSONArray?): List<VisualAsset> = buildList {
        if (array == null) return@buildList
        for (i in 0 until array.length()) {
            val item=array.optJSONObject(i) ?: continue
            add(VisualAsset(item.optString("id"),item.optString("name"),extractImageUrl(item),item.optString("description").takeIf{it.isNotBlank()}))
        }
    }
    private fun extractImageUrl(item: JSONObject): String? {
        val raw=item.opt("imageUrl")
        return when(raw) {
            is String -> raw.takeIf{it.isNotBlank()}
            is JSONObject -> sequenceOf("icon","portrait","avatar","map","thumbnail").mapNotNull{raw.optString(it).takeIf(String::isNotBlank)}.firstOrNull()
            else -> null
        }
    }
}
