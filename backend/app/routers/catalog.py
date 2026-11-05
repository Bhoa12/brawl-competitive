from fastapi import APIRouter, Query
from app.models.game import Brawler, Map
from app.services.brawlapi import brawl_api
router = APIRouter(prefix="/catalog", tags=["catalog"])

@router.get("/brawlers", response_model=list[Brawler])
async def get_brawlers():
    return await brawl_api.brawlers()

@router.get("/maps", response_model=list[Map])
async def get_maps(active_only: bool = Query(False)):
    return await brawl_api.maps(active_only=active_only)
