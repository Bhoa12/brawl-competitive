package com.bhoa.brawlcompetitive.data

/**
 * Repository boundary for the remote Brawl Competitive API.
 * The UI never needs to know which external provider supplies the data.
 */
interface CatalogRepository {
    suspend fun getBrawlers(): List<Brawler>
}

class DemoCatalogRepository : CatalogRepository {
    override suspend fun getBrawlers(): List<Brawler> = listOf(
        Brawler("16000000", "Shelly", "https://cdn.brawlify.com/brawlers/borderless/16000000.png", "Una luchadora versátil de corto alcance.", "Damage Dealer", "Starting Brawler"),
        Brawler("16000001", "Colt", "https://cdn.brawlify.com/brawlers/borderless/16000001.png", "Especialista de largo alcance.", "Damage Dealer", "Rare"),
        Brawler("16000002", "Bull", "https://cdn.brawlify.com/brawlers/borderless/16000002.png", "Tanque explosivo de corto alcance.", "Tank", "Rare")
    )
}
