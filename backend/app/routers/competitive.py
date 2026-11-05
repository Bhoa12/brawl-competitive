from fastapi import APIRouter, HTTPException

from app.models.competitive import CompetitiveBuild, CounterRecommendation
from app.services.competitive_repository import competitive_repository

router = APIRouter(prefix="/competitive", tags=["competitive"])


@router.get("/brawlers/{brawler_id}/build", response_model=CompetitiveBuild)
async def get_build(brawler_id: int):
    build = competitive_repository.build_for(brawler_id)
    if not build:
        raise HTTPException(status_code=404, detail="No build available yet")
    return build


@router.get("/brawlers/{brawler_id}/counters", response_model=list[CounterRecommendation])
async def get_counters(brawler_id: int):
    return competitive_repository.counters_for(brawler_id)
