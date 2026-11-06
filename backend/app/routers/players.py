from fastapi import APIRouter, HTTPException
from app.services.official_api import official_api

router = APIRouter(prefix="/players", tags=["players"])

@router.get("/{player_tag}")
async def get_player(player_tag: str):
    try:
        return await official_api.player(player_tag)
    except RuntimeError as exc:
        raise HTTPException(status_code=503, detail=str(exc))
    except Exception as exc:
        status = getattr(getattr(exc, "response", None), "status_code", 502)
        raise HTTPException(status_code=status, detail="No se pudo obtener el perfil del jugador")
