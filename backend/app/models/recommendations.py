from pydantic import BaseModel, Field

class BuildRecommendation(BaseModel):
    gadget_id: str | None = None
    star_power_id: str | None = None
    gear_ids: list[str] = Field(default_factory=list)

class CounterRecommendation(BaseModel):
    target_brawler_id: str
    counter_brawler_id: str
    score: float
    build: BuildRecommendation
    context_scope: str = "global"
    patch: str | None = None

class BrawlerRecommendationResponse(BaseModel):
    brawler_id: str
    counters: list[CounterRecommendation] = Field(default_factory=list)
