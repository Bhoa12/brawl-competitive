package com.bhoa.brawlcompetitive.data

data class CompetitiveBuild(
    val gadget: String? = null,
    val starPower: String? = null,
    val gears: List<String> = emptyList(),
    val hypercharge: Boolean = false,
    val source: String = "Pendiente de fuente analítica"
)

data class MetaEntry(
    val brawlerId: Int,
    val brawlerName: String,
    val mapName: String? = null,
    val mode: String? = null,
    val rank: String = "General",
    val subRank: String? = null,
    val pickRate: Double? = null,
    val winRate: Double? = null,
    val sampleSize: Int? = null,
    val updatedAt: Long? = null,
    val build: CompetitiveBuild? = null
)
