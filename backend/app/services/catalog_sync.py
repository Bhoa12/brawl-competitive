import hashlib
from datetime import datetime, timezone
import httpx

BRAWL_API_BASE = "https://api.brawlapi.com"

class CatalogSync:
    async def fingerprint(self) -> dict:
        """
        Produces stable fingerprints for the current game catalog.
        BrawlAPI rebuilds its static game data after Brawl Stars updates.
        """
        async with httpx.AsyncClient(timeout=30) as client:
            brawlers = (await client.get(f"{BRAWL_API_BASE}/v1/brawlers")).json()
            maps = (await client.get(f"{BRAWL_API_BASE}/v1/maps")).json()
        raw = repr({
            "brawlers": [(x["id"], x.get("version")) for x in brawlers.get("list", [])],
            "maps": [(x["id"], x.get("version")) for x in maps.get("list", [])],
        }).encode()
        return {
            "fingerprint": hashlib.sha256(raw).hexdigest(),
            "checked_at": datetime.now(timezone.utc).isoformat(),
            "brawler_count": len(brawlers.get("list", [])),
            "map_count": len(maps.get("list", [])),
        }

catalog_sync = CatalogSync()
