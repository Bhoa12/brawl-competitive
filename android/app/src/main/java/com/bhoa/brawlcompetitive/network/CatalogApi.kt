package com.bhoa.brawlcompetitive.network

import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONArray
import org.json.JSONObject

data class RemoteBrawler(val id: String, val name: String, val imageUrl: String)
data class RemoteMap(val id: String, val name: String, val mode: String?, val imageUrl: String)
data class RemoteCatalog(val version: String, val brawlers: List<RemoteBrawler>, val maps: List<RemoteMap>)

class CatalogApi(private val baseUrl: String) {
    fun fetch(): RemoteCatalog {
        val connection = (URL(baseUrl.trimEnd('/') + "/catalog").openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }
        try {
            if (connection.responseCode !in 200..299) error("Catalog request failed: " + connection.responseCode)
            val root = JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
            fun imageUrl(item: JSONObject): String =
                item.optJSONObject("image")?.optString("url") ?: ""

            val brawlers = root.optJSONArray("brawlers") ?: JSONArray()
            val maps = root.optJSONArray("maps") ?: JSONArray()
            return RemoteCatalog(
                version = root.optString("version"),
                brawlers = buildList {
                    for (i in 0 until brawlers.length()) {
                        val item = brawlers.getJSONObject(i)
                        add(RemoteBrawler(item.opt("id").toString(), item.optString("name"), imageUrl(item)))
                    }
                },
                maps = buildList {
                    for (i in 0 until maps.length()) {
                        val item = maps.getJSONObject(i)
                        add(RemoteMap(item.opt("id").toString(), item.optString("name"), item.optString("mode").ifBlank { null }, imageUrl(item)))
                    }
                }
            )
        } finally { connection.disconnect() }
    }
}
