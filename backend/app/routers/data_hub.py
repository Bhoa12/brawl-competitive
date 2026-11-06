from fastapi import APIRouter, HTTPException
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


@router.get("/competitive-pool")
async def competitive_pool():
    catalog = catalog_service.get()
    return {
        "version": COMPETITIVE_POOL_VERSION.season,
        "updated_at": COMPETITIVE_POOL_VERSION.updated_at,
        "source": COMPETITIVE_POOL_VERSION.source,
        "maps": [
            {"id": game_map.id, "name": game_map.name, "mode": game_map.mode, "image": game_map.image.url}
            for game_map in catalog.maps if game_map.active
        ],
    }

@router.get("/brawlers/{brawler_id}/summary")
async def brawler_summary(brawler_id: str):
    catalog = catalog_service.get()
    brawler = next((item for item in catalog.brawlers if str(item.id) == str(brawler_id)), None)
    if not brawler:
        raise HTTPException(status_code=404, detail="Brawler not found")
    return {
        "id": brawler.id,
        "name": brawler.name,
        "image": brawler.image.url,
        "data_scope": "catalog",
        "updated_at": catalog.updated_at.isoformat(),
    }
