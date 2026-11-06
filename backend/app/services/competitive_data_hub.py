from datetime import datetime, timezone
from app.models.catalog import CatalogBrawler, CatalogImage, CatalogMap
from app.services.catalog_service import catalog_service
from app.services.providers.brawlapi_provider import brawlapi_provider

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
                mode=(item.get("gameMode") or {}).get("name"),
                image=CatalogImage(url=((item.get("imageUrl") or {}).get("map"))),
                active=False,
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
            "competitive_maps_active": 0,
        }

competitive_data_hub = CompetitiveDataHub()
