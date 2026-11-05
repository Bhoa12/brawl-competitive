package com.bhoa.brawlcompetitive.data

data class BrawlerCatalogItem(
    val id: String,
    val name: String,
    val imageUrl: String
)

object BrawlerCatalog {
    // Seed catalog. The automatic catalog sync will replace this static mapping.
    private val items = listOf(
        BrawlerCatalogItem("16000000", "Shelly", "https://cdn.brawlify.com/brawlers/borderless/16000000.png"),
        BrawlerCatalogItem("16000001", "Colt", "https://cdn.brawlify.com/brawlers/borderless/16000001.png"),
        BrawlerCatalogItem("16000002", "Bull", "https://cdn.brawlify.com/brawlers/borderless/16000002.png"),
        BrawlerCatalogItem("16000003", "Jessie", "https://cdn.brawlify.com/brawlers/borderless/16000003.png")
    )

    private val byId = items.associateBy { it.id }

    fun find(id: String): BrawlerCatalogItem? = byId[id]

    fun fallback(id: String) = BrawlerCatalogItem(
        id = id,
        name = "Brawler " + id,
        imageUrl = ""
    )
}
