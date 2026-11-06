import asyncio
import os

from fastapi import APIRouter, HTTPException
from app.models.ingestion import IngestionResult
from app.services.battle_ingestion import battle_ingestion

router = APIRouter(prefix="ingestion", tags=["ingestion"])


def _configured_player_tags() -> list[str]:
    raw = os.getenv("COMPETITIVE_PLAYER_TAGS", "")
    seen: set[str] = set()
    tags: list[str] = []
    for value in raw.split(","):
        tag = value.strip().upper()
        if tag and tag not in seen:
            seen.add(tag)
            tags.append(tag)
    return tags


@router.post("/players/{player_tag}/battlelog", response_model=IngestionResult)
async def ingest_player_battlelog(player_tag: str):
    try:
        return await battle_ingestion.collect_player(player_tag)
    except RuntimeError as exc:
        raise HTTPException(status_code=503, detail=str(exc))


@router.post("/sync/configured")
async def sync_configured_players():
    """Collect battle logs for the server-managed competitive sample.

    Configure COMPETITIVE_PLAYER_TAGS as a comma-separated environment variable.
    This keeps player selection out of the mobile app and makes scheduled syncs possible.
    """
    tags = _configured_player_tags()
    if not tags:
        raise HTTPException(
            status_code=503,
            detail="No competitive player sample configured. Set COMPETITIVE_PLAYER_TAGS.",
        )

    results = await asyncio.gather(
        *(battle_ingestion.collect_player(tag) for tag in tags),
        return_exceptions=True,
    )
    completed = []
    failures = []
    for tag, result in zip(tags, results):
        if isinstance(result, Exception):
            failures.append({"player_tag": tag, "error": str(result)})
        else:
            completed.append(result.model_dump())

    return {
        "configured_players": len(tags),
        "completed": completed,
        "failures": failures,
    }
