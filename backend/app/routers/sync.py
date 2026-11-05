from fastapi import APIRouter
from app.services.catalog_sync import catalog_sync

router = APIRouter(prefix="/sync", tags=["sync"])

@router.get("/status")
async def sync_status():
    return await catalog_sync.fingerprint()
