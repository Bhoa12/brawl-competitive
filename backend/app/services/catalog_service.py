from datetime import datetime, timezone
from app.models.catalog import CatalogBrawler, CatalogImage, CatalogMap, GameCatalog

class CatalogService:
    """
    Catalog boundary. Upstream sync adapters should normalize official or
    otherwise licensed data into this model; the APK never needs upstream keys.
    """
    def __init__(self):
        self._catalog = GameCatalog(
            version="bootstrap",
            updated_at=datetime.now(timezone.utc),
            brawlers=[],
            maps=[]
        )

    def get(self) -> GameCatalog:
        return self._catalog

    def replace(self, *, version: str, brawlers: list[CatalogBrawler], maps: list[CatalogMap]) -> GameCatalog:
        self._catalog = GameCatalog(
            version=version,
            updated_at=datetime.now(timezone.utc),
            brawlers=brawlers,
            maps=maps
        )
        return self._catalog

catalog_service = CatalogService()
