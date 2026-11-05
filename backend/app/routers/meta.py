from fastapi import APIRouter, Query

from app.models.meta import MetaSnapshot
from app.services.meta_engine import meta_engine

router = APIRouter(prefix="/meta", tags=["meta"])


@router.get("/snapshot", response_model=MetaSnapshot)
async def get_meta_snapshot(
    map_id: int | None = Query(None),
    mode_id: int | None = Query(None),
    rank: str | None = Query(None),
    subrank: str | None = Query(None),
    patch: str = Query("unknown"),
):
    return meta_engine.snapshot(
        map_id=map_id,
        mode_id=mode_id,
        rank=rank,
        subrank=subrank,
        patch=patch,
    )
