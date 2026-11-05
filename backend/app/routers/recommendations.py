from fastapi import APIRouter, Query
from app.services.recommendation_service import recommendation_service

router = APIRouter(prefix="/recommendations", tags=["recommendations"])

@router.get("/brawlers/{brawler_id}/counters")
async def counters(
    brawler_id: str,
    scope: str = Query("global"),
    patch: str | None = None,
):
    return recommendation_service.counters_for(brawler_id, scope=scope, patch=patch)
