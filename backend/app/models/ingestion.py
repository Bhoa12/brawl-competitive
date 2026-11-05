from datetime import datetime
from pydantic import BaseModel, Field


class BattleSample(BaseModel):
    battle_time: datetime
    event_id: int | None = None
    map_id: int | None = None
    mode: str | None = None
    battle_type: str | None = None
    brawler_ids: list[int] = Field(default_factory=list)
    winner_brawler_ids: list[int] = Field(default_factory=list)
    source_player_tag: str | None = None


class IngestionResult(BaseModel):
    source: str
    fetched: int
    accepted: int
    rejected: int
    collected_at: datetime
