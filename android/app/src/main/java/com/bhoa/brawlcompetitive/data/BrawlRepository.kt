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
        } catch (_: Exception) { fallback() } finally { connection.disconnect() }
    }

    private fun parse(array: JSONArray) = buildList {
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val gadgets = parseAssets(item.optJSONArray("gadgets"))
            val stars = parseAssets(item.optJSONArray("starPowers"))
            add(Brawler(
                id = item.optInt("id"),
                name = item.optString("name"),
                imageUrl = item.optString("imageUrl").takeIf { it.isNotBlank() },
                description = item.optString("description").takeIf { it.isNotBlank() },
                brawlerClass = item.optJSONObject("class")?.optString("name"),
                rarity = item.optJSONObject("rarity")?.optString("name"),
                gadgets = gadgets,
                starPowers = stars
            ))
        }
    }

    private fun parseAssets(array: JSONArray?): List<VisualAsset> = buildList {
        if (array == null) return@buildList
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            add(VisualAsset(item.optString("name"), item.optString("imageUrl").takeIf { it.isNotBlank() },
                item.optString("description").takeIf { it.isNotBlank() }))
        }
    }

    private fun fallback() = listOf("Shelly","Nita","Colt","Bull","Jessie","Brock","Dynamike","Bo","Tick","8-Bit","Emz","El Primo","Barley","Poco","Rosa","Rico","Darryl","Penny","Carl","Jacky","Piper","Pam","Frank","Bibi","Bea","Nani","Edgar","Griff","Grom","Bonnie","Gus","Sam","Mandy","Maisie","Hank","Pearl","Larry & Lawrie","Angelo","Melodie","Lily","Clancy","Moe","Kenji","Shade","Juju","Meeple","Ollie","Lumi").mapIndexed { index, name -> Brawler(index + 1, name) }
}