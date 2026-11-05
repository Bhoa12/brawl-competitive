from pydantic import BaseModel


class BuildChoice(BaseModel):
    id: str
    name: str
    image_url: str | None = None


class CompetitiveBuild(BaseModel):
    brawler_id: int
    brawler_name: str
    gadget: BuildChoice | None = None
    star_power: BuildChoice | None = None
    gear_1: BuildChoice | None = None
    gear_2: BuildChoice | None = None
    hypercharge: BuildChoice | None = None
    map_id: int | None = None
    mode_id: int | None = None
    rank: str | None = None
    patch: str | None = None


class CounterRecommendation(BaseModel):
    target_brawler_id: int
    target_brawler_name: str
    counter_brawler_id: int
    counter_brawler_name: str
    confidence: float
    reason: str
    build: CompetitiveBuild
    map_id: int | None = None
    mode_id: int | None = None
    rank: str | None = None
    patch: str | None = None
