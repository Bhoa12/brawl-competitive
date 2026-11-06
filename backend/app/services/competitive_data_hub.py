from datetime import datetime, timezone
from dataclasses import dataclass
from app.models.catalog import CatalogBrawler, CatalogImage, CatalogMap
from app.services.catalog_service import catalog_service
from app.services.providers.brawlapi_provider import brawlapi_provider

RETIRED_BRAWLERS = {"BUZZ LIGHTYEAR"}

@dataclass(frozen=True)
class CompetitivePoolVersion:
    season: str
    updated_at: str
    source: str

# Deliberately separate the curated Ranked pool from the full historical map catalog.
# The pool must be updated from a verified seasonal source rather than treating every
# map returned by a static API as competitive.
COMPETITIVE_POOL_VERSION = CompetitivePoolVersion(
    season="curated-current",
    updated_at="2026-11-06",
    source="Competitive Data Hub",
)

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

def image_url(item: dict) -> str | None:
    value = item.get("imageUrl")
    if isinstance(value, str):
        return value or None
    if isinstance(value, dict):
        for key in ("portrait", "icon", "map", "thumbnail", "avatar"):
            candidate = value.get(key)
            if candidate:
                return candidate
    return None

class CompetitiveDataHub:
    async def refresh_catalog(self) -> dict:
        raw_brawlers, raw_maps, source = await brawlapi_provider.fetch_catalog()

        brawlers = [
            CatalogBrawler(
                id=str(item.get("id", "")),
                name=item.get("name", ""),
                image=CatalogImage(url=image_url(item)),
            )
            for item in raw_brawlers
            if item.get("name") and item.get("released", True)
            and item.get("name", "").strip().upper() not in RETIRED_BRAWLERS
        ]

        maps = []
        for item in raw_maps:
            name = item.get("name", "")
            mode = competitive_mode_for(name)
            if not name or mode is None:
                continue
            game_mode = item.get("gameMode") or {}
            maps.append(CatalogMap(
                id=str(item.get("id", "")),
                name=name,
                mode=mode or game_mode.get("name"),
                image=CatalogImage(url=image_url(item)),
                active=True,
            ))

        catalog = catalog_service.replace(
            version=f"{source.source}-{datetime.now(timezone.utc).date().isoformat()}",
            brawlers=brawlers,
            maps=maps,
        )
        return {
            "source": source.source,
            "data_policy": "official_or_structured_catalog; third-party sites are reference inputs for validation and metadata only",
            "pool_version": COMPETITIVE_POOL_VERSION.season,
            "pool_updated_at": COMPETITIVE_POOL_VERSION.updated_at,
            "pool_source": COMPETITIVE_POOL_VERSION.source,
            "updated_at": catalog.updated_at.isoformat(),
            "brawlers": len(brawlers),
            "competitive_maps_active": len(maps),
        }

competitive_data_hub = CompetitiveDataHub()
