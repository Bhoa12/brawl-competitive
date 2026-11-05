from fastapi import APIRouter
from app.services.stats_aggregator import stats_aggregator

router = APIRouter(prefix="/statistics", tags=["statistics"])

@router.get("/summary")
async def summary():
    return stats_aggregator.aggregate()
