from fastapi import APIRouter
from app.services.competitive_data_hub import competitive_data_hub

router = APIRouter(prefix="/data", tags=["data"])

@router.post("/refresh")
async def refresh_data():
    return await competitive_data_hub.refresh_catalog()
