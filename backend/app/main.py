from fastapi import FastAPI
from app.routers.health import router as health_router

app = FastAPI(
    title="Brawl Competitive API",
    version="0.1.0",
    description="Backend for competitive Brawl Stars analysis.",
)

app.include_router(health_router)


@app.get("/")
async def root():
    return {
        "service": "brawl-competitive",
        "status": "online",
        "version": app.version,
    }
