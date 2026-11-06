from fastapi import FastAPI
from app.init_db import init_db
from app.routers.catalog import router as catalog_router
from app.routers.recommendations import router as recommendations_router
from app.routers.competitive import router as competitive_router
from app.routers.health import router as health_router
from app.routers.meta import router as meta_router
from app.routers.sync import router as sync_router
from app.routers.ingestion import router as ingestion_router
from app.routers.statistics import router as statistics_router
from app.routers.snapshots import router as snapshots_router
from app.routers.persistent_statistics import router as persistent_statistics_router
from app.routers.data_hub import router as data_hub_router
from app.routers.players import router as players_router
from app.services.competitive_data_hub import competitive_data_hub

init_db()

app = FastAPI(
    title="Brawl Competitive API",
    version="0.12.0",
    description="Backend for competitive Brawl Stars analysis.",
)

app.include_router(health_router)
app.include_router(catalog_router)
app.include_router(recommendations_router)
app.include_router(competitive_router)
app.include_router(meta_router)
app.include_router(sync_router)
app.include_router(ingestion_router)
app.include_router(statistics_router)
app.include_router(snapshots_router)
app.include_router(persistent_statistics_router)
app.include_router(data_hub_router)
app.include_router(players_router)


@app.on_event("startup")
async def refresh_catalog_on_startup():
    try:
        await competitive_data_hub.refresh_catalog()
    except Exception:
        pass


@app.get("/")
async def root():
    return {
        "service": "brawl-competitive",
        "status": "online",
        "version": app.version,
        "visual_first": True,
    }
