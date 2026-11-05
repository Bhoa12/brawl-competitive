from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from app.db import get_db
from app.services.persistent_stats_aggregator import persistent_stats_aggregator
from app.services.persistent_snapshot_service import persistent_snapshot_service

router = APIRouter(prefix="/analytics", tags=["analytics"])

@router.get("/summary")
async def summary(
    map_id: int | None = None,
    mode: str | None = None,
    db: Session = Depends(get_db),
):
    return persistent_stats_aggregator.aggregate(db, map_id=map_id, mode=mode)

@router.post("/snapshot")
async def snapshot(
    patch: str = Query(...),
    scope: str = Query("global"),
    map_id: int | None = None,
    mode: str | None = None,
    db: Session = Depends(get_db),
):
    return persistent_snapshot_service.create(db, patch=patch, scope=scope, map_id=map_id, mode=mode)
