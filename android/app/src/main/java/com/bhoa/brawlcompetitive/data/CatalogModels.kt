package com.bhoa.brawlcompetitive.data

data class VisualAsset(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val description: String? = null
)

data class Brawler(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val description: String?,
    val brawlerClass: String?,
    val rarity: String?,
    val gadgets: List<VisualAsset> = emptyList(),
    val starPowers: List<VisualAsset> = emptyList()
)
