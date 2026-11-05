from fastapi import APIRouter
from app.services.catalog_service import catalog_service

router = APIRouter(prefix="/catalog", tags=["catalog"])

@router.get("")
async def catalog():
    return catalog_service.get()

@router.get("/brawlers")
async def brawlers():
    return catalog_service.get().brawlers

@router.get("/maps")
async def maps():
    return catalog_service.get().maps
