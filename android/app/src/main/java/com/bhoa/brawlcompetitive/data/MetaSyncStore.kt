package com.bhoa.brawlcompetitive.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class MetaSnapshot(val entries: List<MetaEntry>, val savedAt: Long)

object MetaSyncStore {
    private const val PREFS = "competitive_sync"
    private const val KEY_META = "cached_meta"
    private const val KEY_TIME = "cached_meta_time"

    fun save(context: Context, entries: List<MetaEntry>) {
        val array = JSONArray()
        entries.forEach { entry ->
            array.put(JSONObject().apply {
                put("brawlerId", entry.brawlerId)
                put("brawlerName", entry.brawlerName)
                putOpt("mapName", entry.mapName)
                putOpt("mode", entry.mode)
                put("rank", entry.rank)
                putOpt("subRank", entry.subRank)
                putOpt("pickRate", entry.pickRate)
                putOpt("winRate", entry.winRate)
                putOpt("sampleSize", entry.sampleSize)
                putOpt("updatedAt", entry.updatedAt)
            })
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_META, array.toString())
            .putLong(KEY_TIME, System.currentTimeMillis())
            .apply()
    }

    fun load(context: Context): MetaSnapshot? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_META, null) ?: return null
        val array = try { JSONArray(raw) } catch (_: Exception) { return null }
        val entries = buildList {
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
        return MetaSnapshot(entries, prefs.getLong(KEY_TIME, 0L))
    }

    private fun JSONObject.takeNullableDouble(name: String): Double? =
        if (isNull(name)) null else optDouble(name).takeIf { !it.isNaN() }
}