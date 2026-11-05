from datetime import datetime
from pydantic import BaseModel

class CatalogImage(BaseModel):
    url: str | None = None

class CatalogBrawler(BaseModel):
    id: str
    name: str
    image: CatalogImage = CatalogImage()
    updated_at: datetime | None = None

class CatalogMap(BaseModel):
    id: str
    name: str
    mode: str | None = None
    image: CatalogImage = CatalogImage()
    active: bool = True

class GameCatalog(BaseModel):
    version: str
    updated_at: datetime
    brawlers: list[CatalogBrawler] = []
    maps: list[CatalogMap] = []
