from fastapi import APIRouter, HTTPException
from app.models.ingestion import IngestionResult
from app.services.battle_ingestion import battle_ingestion

router = APIRouter(prefix="/ingestion", tags=["ingestion"])


@router.post("/players/{player_tag}/battlelog", response_model=IngestionResult)
async def ingest_player_battlelog(player_tag: str):
    try:
        return await battle_ingestion.collect_player(player_tag)
    except RuntimeError as exc:
        raise HTTPException(status_code=503, detail=str(exc))
