package com.bhoa.brawlcompetitive.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object BrawlRepository {
    suspend fun loadBrawlers(): List<Brawler> = withContext(Dispatchers.IO) {
        val connection = URL("https://api.brawlapi.com/v1/brawlers").openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 15000
        try {
            if (connection.responseCode !in 200..299) return@withContext fallback()
            val raw = connection.inputStream.bufferedReader().use { it.readText() }
            parse(JSONObject(raw).optJSONArray("list") ?: JSONArray()).ifEmpty { fallback() }
        } catch (_: Exception) {
            fallback()
        } finally {
            connection.disconnect()
        }
    }

    private fun parse(array: JSONArray): List<Brawler> = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val name = item.optString("name")
            if (name.equals("Buzz Lightyear", ignoreCase = true)) continue
            val gadgets = parseAssets(item.optJSONArray("gadgets"))
            val stars = parseAssets(item.optJSONArray("starPowers"))
            add(
                Brawler(
                    id = item.optString("id"),
                    name = name,
                    imageUrl = extractImageUrl(item),
                    description = item.optString("description").takeIf { it.isNotBlank() },
                    brawlerClass = item.optJSONObject("class")?.optString("name"),
                    rarity = item.optJSONObject("rarity")?.optString("name"),
                    gadgets = gadgets,
                    starPowers = stars
                )
            )
        }
    }

    private fun parseAssets(array: JSONArray?): List<VisualAsset> = buildList {
        if (array == null) return@buildList
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            add(
                VisualAsset(
                    id = item.optString("id"),
                    name = item.optString("name"),
                    imageUrl = extractImageUrl(item),
                    description = item.optString("description").takeIf { it.isNotBlank() }
                )
            )
        }
    }

    private fun extractImageUrl(item: JSONObject): String? {
        val raw = item.opt("imageUrl")
        return when (raw) {
            is String -> raw.takeIf { it.isNotBlank() }
            is JSONObject -> sequenceOf("icon", "portrait", "avatar").mapNotNull { key ->
                raw.optString(key).takeIf { it.isNotBlank() }
            }.firstOrNull()
            else -> null
        }
    }

    private fun fallback(): List<Brawler> =
        listOf("Shelly","Nita","Colt","Bull","Jessie","Brock","Dynamike","Bo","Tick","8-Bit","Emz","El Primo","Barley","Poco","Rosa","Rico","Darryl","Penny","Carl","Jacky","Piper","Pam","Frank","Bibi","Bea","Nani","Edgar","Griff","Grom","Bonnie","Gus","Sam","Mandy","Maisie","Hank","Pearl","Larry & Lawrie","Angelo","Melodie","Lily","Clancy","Moe","Kenji","Shade","Juju","Meeple","Ollie","Lumi")
            .mapIndexed { index, name ->
                Brawler(
                    id = (index + 1).toString(),
                    name = name,
                    imageUrl = null,
                    description = null,
                    brawlerClass = null,
                    rarity = null
                )
            }
}
