from datetime import datetime, timezone
from app.models.catalog import CatalogBrawler, CatalogImage, CatalogMap
from app.services.catalog_service import catalog_service
from app.services.providers.brawlapi_provider import brawlapi_provider

# Current Ranked pool is maintained separately from the historical map catalog.
COMPETITIVE_MAP_POOL = {
    "Gem Grab": {"Double Swoosh", "Undermine", "Gem Fort", "Hard Rock Mine"},
    "Brawl Ball": {"Center Stage", "Sneaky Fields", "Triple Dribble", "Pinball Dreams"},
    "Heist": {"Safe Zone", "Hot Potato", "Bridge Too Far", "Kaboom Canyon"},
    "Bounty": {"Dry Season", "Hideout", "Layer Cake", "Shooting Star"},
    "Hot Zone": {"Dueling Beetles", "Open Business", "Parallel Plays", "Ring of Fire"},
    "Knockout": {"Flaring Phoenix", "New Horizons", "Out in the Open", "Goldarm Gulch", "Flowing Springs", "Belle's Rock"},
}

def competitive_mode_for(name: str) -> str | None:
    for mode, maps in COMPETITIVE_MAP_POOL.items():
        if name in maps:
            return mode
    return None

class CompetitiveDataHub:
    """
    Central normalization boundary for game and competitive data.
    Upstream providers can be added without changing Android clients.
    Competitive map activation is intentionally separate from the full
    historical map catalog.
    """

    async def refresh_catalog(self) -> dict:
        raw_brawlers, raw_maps, source = await brawlapi_provider.fetch_catalog()

        brawlers = [
            CatalogBrawler(
                id=str(item.get("id", "")),
                name=item.get("name", ""),
                image=CatalogImage(url=(item.get("imageUrl") or {}).get("portrait")),
            )
            for item in raw_brawlers
            if item.get("name") and item.get("released", True)
        ]

        # The upstream catalog contains historical maps. Until a verified
        # competitive rotation feed is configured, expose maps as inactive
        # rather than incorrectly presenting the entire history as competitive.
        maps = [
            CatalogMap(
                id=str(item.get("id", "")),
                name=item.get("name", ""),
                mode=competitive_mode_for(item.get("name", "")) or (item.get("gameMode") or {}).get("name"),
                image=CatalogImage(url=((item.get("imageUrl") or {}).get("map"))),
                active=competitive_mode_for(item.get("name", "")) is not None,
            )
            for item in raw_maps
            if item.get("name")
        ]

        catalog = catalog_service.replace(
            version=f"{source.source}-{datetime.now(timezone.utc).date().isoformat()}",
            brawlers=brawlers,
            maps=maps,
        )
        return {
            "source": source.source,
            "updated_at": catalog.updated_at.isoformat(),
            "brawlers": len(brawlers),
            "maps_cached": len(maps),
            "competitive_maps_active": sum(1 for game_map in maps if game_map.active),
        }

competitive_data_hub = CompetitiveDataHub()
