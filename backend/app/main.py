from fastapi import FastAPI
from app.routers.catalog import router as catalog_router
from app.routers.competitive import router as competitive_router
from app.routers.health import router as health_router
from app.routers.meta import router as meta_router

app = FastAPI(
    title="Brawl Competitive API",
    version="0.4.0",
    description="Backend for competitive Brawl Stars analysis.",
)

app.include_router(health_router)
app.include_router(catalog_router)
app.include_router(competitive_router)
app.include_router(meta_router)


@app.get("/")
async def root():
    return {
        "service": "brawl-competitive",
        "status": "online",
        "version": app.version,
        "visual_first": True,
    }
