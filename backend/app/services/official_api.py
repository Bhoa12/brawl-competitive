import os
import httpx

DEFAULT_BASE_URL = "https://bsproxy.royaleapi.dev/v1"


class OfficialBrawlStarsClient:
    """Server-side client for the official API or a configured compatible proxy."""

    def __init__(self) -> None:
        self.token = os.getenv("BRAWL_STARS_API_TOKEN")
        self.base_url = os.getenv(
            "BRAWL_STARS_API_BASE_URL", DEFAULT_BASE_URL
        ).rstrip("/")

    def _headers(self) -> dict[str, str]:
        if not self.token:
            raise RuntimeError("BRAWL_STARS_API_TOKEN is not configured")
        return {
            "Authorization": f"Bearer {self.token}",
            "Accept": "application/json",
        }

    async def battle_log(self, player_tag: str) -> dict:
        encoded_tag = player_tag if player_tag.startswith("%23") else "%23" + player_tag.lstrip("#")
        async with httpx.AsyncClient(timeout=20) as client:
            response = await client.get(
                f"{self.base_url}/players/{encoded_tag}/battlelog",
                headers=self._headers(),
            )
            response.raise_for_status()
            return response.json()


official_api = OfficialBrawlStarsClient()
