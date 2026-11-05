from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from app.db import get_db
from app.models.competitive_context import CompetitiveContext
from app.services.persistent_stats_aggregator import persistent_stats_aggregator
from app.services.persistent_snapshot_service import persistent_snapshot_service

router = APIRouter(prefix="/analytics", tags=["analytics"])

def build_context(map_id, mode, rank, subrank, patch):
    return CompetitiveContext(map_id=map_id, mode=mode, rank=rank, subrank=subrank, patch=patch)

@router.get("/summary")
async def summary(
    map_id: int | None = None,
    mode: str | None = None,
    rank: str | None = None,
    subrank: str | None = None,
    patch: str | None = None,
    db: Session = Depends(get_db),
):
    return persistent_stats_aggregator.aggregate(db, build_context(map_id, mode, rank, subrank, patch))

@router.post("/snapshot")
async def snapshot(
    patch: str = Query(...),
    map_id: int | None = None,
    mode: str | None = None,
    rank: str | None = None,
    subrank: str | None = None,
    db: Session = Depends(get_db),
):
    context = build_context(map_id, mode, rank, subrank, patch)
    return persistent_snapshot_service.create(
        db, patch=patch, scope=context.scope_key(), map_id=map_id, mode=mode
    )
