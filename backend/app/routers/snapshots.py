from fastapi import APIRouter, Query
from app.models.snapshots import MetaSnapshotRecord
from app.services.snapshot_repository import snapshot_repository
from app.services.snapshot_service import snapshot_service

router = APIRouter(prefix="/snapshots", tags=["snapshots"])

@router.post("", response_model=MetaSnapshotRecord)
async def create_snapshot(patch: str = Query(...), scope: str = Query("global")):
    return snapshot_service.create(patch=patch, scope=scope)

@router.get("/latest", response_model=MetaSnapshotRecord | None)
async def latest_snapshot(scope: str | None = None):
    return snapshot_repository.latest(scope)

@router.get("/history", response_model=list[MetaSnapshotRecord])
async def snapshot_history(limit: int = Query(20, ge=1, le=100)):
    return snapshot_repository.history(limit)

@router.get("/compare")
async def compare_snapshots(scope: str = Query("global")):
    return snapshot_service.compare_latest(scope)
