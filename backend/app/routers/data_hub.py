from fastapi import APIRouter
from app.services.catalog_service import catalog_service
from app.services.competitive_data_hub import (
    COMPETITIVE_MAP_POOL,
    COMPETITIVE_POOL_VERSION,
    competitive_data_hub,
)

router = APIRouter(prefix="/data", tags=["data"])

@router.post("/refresh")
async def refresh_data():
    return await competitive_data_hub.refresh_catalog()

@router.get("/status")
async def data_status():
    catalog = catalog_service.get()
    ready = len(catalog.brawlers) > 0
    return {
        "status": "ready" if ready else "degraded",
        "catalog_version": catalog.version,
        "catalog_updated_at": catalog.updated_at.isoformat(),
        "brawlers": len(catalog.brawlers),
        "competitive_maps": sum(1 for game_map in catalog.maps if game_map.active),
        "pool_version": COMPETITIVE_POOL_VERSION.season,
        "pool_updated_at": COMPETITIVE_POOL_VERSION.updated_at,
        "pool_source": COMPETITIVE_POOL_VERSION.source,
        "modes": {mode: len(maps) for mode, maps in COMPETITIVE_MAP_POOL.items()},
    }
