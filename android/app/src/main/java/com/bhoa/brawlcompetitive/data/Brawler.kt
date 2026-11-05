package com.bhoa.brawlcompetitive.data

data class VisualAsset(val name: String, val imageUrl: String? = null, val description: String? = null)

data class Brawler(
    val id: Int, val name: String, val imageUrl: String? = null,
    val description: String? = null, val brawlerClass: String? = null,
    val rarity: String? = null, val gadgets: List<VisualAsset> = emptyList(),
    val starPowers: List<VisualAsset> = emptyList()
)