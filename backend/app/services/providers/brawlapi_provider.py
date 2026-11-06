from dataclasses import dataclass
from typing import Any
import asyncio
import httpx

BRAWL_API_BASE = "https://api.brawlapi.com"

@dataclass
class SourceRecord:
    source: str
    updated_at: str | None = None
    confidence: str = "public-static"

class BrawlApiProvider:
    source_name = "BrawlAPI"

    async def fetch_catalog(self) -> tuple[list[dict[str, Any]], list[dict[str, Any]], SourceRecord]:
        timeout = httpx.Timeout(30.0, connect=10.0)
        headers = {"Accept": "application/json", "User-Agent": "BrawlCompetitive/1.0"}
        async with httpx.AsyncClient(timeout=timeout, headers=headers, follow_redirects=True) as client:
            brawlers_response, maps_response = await asyncio.gather(
                client.get(f"{BRAWL_API_BASE}/v1/brawlers"),
                client.get(f"{BRAWL_API_BASE}/v1/maps"),
            )
        brawlers_response.raise_for_status()
        maps_response.raise_for_status()
        return (
            brawlers_response.json().get("list", []),
            maps_response.json().get("list", []),
            SourceRecord(source=self.source_name),
        )

brawlapi_provider = BrawlApiProvider()
