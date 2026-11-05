from pydantic import BaseModel, Field

class VisualAsset(BaseModel):
    id: int | str
    name: str
    image_url: str | None = None
    description: str | None = None

class Brawler(BaseModel):
    id: int
    name: str
    image_url: str | None = None
    image_url_alt: str | None = None
    description: str | None = None
    brawler_class: str | None = Field(default=None, alias="class")
    rarity: str | None = None
    gadgets: list[VisualAsset] = []
    star_powers: list[VisualAsset] = []
    model_config = {"populate_by_name": True}

class GameMode(VisualAsset):
    disabled: bool = False

class Map(BaseModel):
    id: int
    name: str
    image_url: str | None = None
    disabled: bool = False
    game_mode: GameMode | None = None
