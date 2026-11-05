package com.bhoa.brawlcompetitive.network

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL

data class AnalyticsRow(
    val brawlerId: String,
    val picks: Int,
    val pickRate: Float,
    val wins: Int,
    val winRate: Float
)

data class AnalyticsResponse(
    val sampleSize: Int,
    val updatedAt: String,
    val rows: List<AnalyticsRow>
)

class CompetitiveApi(private val baseUrl: String) {
    fun fetchSummary(
        mapId: Int? = null,
        mode: String? = null,
        rank: String? = null,
        subrank: String? = null,
        patch: String? = null
    ): AnalyticsResponse {
        val parameters = listOf(
            "map_id" to mapId?.toString(),
            "mode" to mode,
            "rank" to rank,
            "subrank" to subrank,
            "patch" to patch
        ).filter { it.second != null }
            .joinToString("&") { (key, value) -> key + "=" + URLEncoder.encode(value, "UTF-8") }

        val url = URL(baseUrl.trimEnd('/') + "/analytics/summary" + if (parameters.isEmpty()) "" else "?" + parameters)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }

        try {
            if (connection.responseCode !in 200..299) error("Analytics request failed: " + connection.responseCode)
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(body)
            val array = root.optJSONArray("rows") ?: JSONArray()
            val rows = buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    add(AnalyticsRow(
                        brawlerId = item.opt("brawler_id").toString(),
                        picks = item.optInt("picks"),
                        pickRate = item.optDouble("pick_rate").toFloat(),
                        wins = item.optInt("wins"),
                        winRate = item.optDouble("win_rate").toFloat()
                    ))
                }
            }
            return AnalyticsResponse(
                sampleSize = root.optInt("sample_size"),
                updatedAt = root.optString("updated_at"),
                rows = rows
            )
        } finally {
            connection.disconnect()
        }
    }
}
