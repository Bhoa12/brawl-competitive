from datetime import datetime
from pydantic import BaseModel, Field


class MetaMetric(BaseModel):
    brawler_id: int
    brawler_name: str
    tier: str
    meta_score: float = Field(ge=0, le=100)
    pick_rate: float | None = Field(default=None, ge=0, le=100)
    win_rate: float | None = Field(default=None, ge=0, le=100)
    trend: float | None = None
    sample_size: int = 0
    updated_at: datetime
    patch: str
    map_id: int | None = None
    mode_id: int | None = None
    rank: str | None = None
    subrank: str | None = None


class MetaSnapshot(BaseModel):
    scope: str
    updated_at: datetime
    patch: str
    sample_size: int
    metrics: list[MetaMetric]
