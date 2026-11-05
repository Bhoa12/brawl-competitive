import httpx
from app.models.game import Brawler, GameMode, Map, VisualAsset
BASE_URL = "https://api.brawlapi.com/v1"

class BrawlApiClient:
    async def _get(self, path: str) -> dict:
        async with httpx.AsyncClient(timeout=20) as client:
            response = await client.get(f"{BASE_URL}{path}")
            response.raise_for_status()
            return response.json()

    @staticmethod
    def _asset(item: dict) -> VisualAsset:
        return VisualAsset(id=item["id"], name=item["name"], image_url=item.get("imageUrl"), description=item.get("description"))

    async def brawlers(self) -> list[Brawler]:
        payload = await self._get("/brawlers")
        return [Brawler(id=i["id"], name=i["name"], image_url=i.get("imageUrl"), image_url_alt=i.get("imageUrl2"), description=i.get("description"), **{"class": i.get("class", {}).get("name")}, rarity=i.get("rarity", {}).get("name"), gadgets=[self._asset(x) for x in i.get("gadgets", [])], star_powers=[self._asset(x) for x in i.get("starPowers", [])]) for i in payload.get("list", [])]

    async def maps(self, active_only: bool = False) -> list[Map]:
        payload = await self._get("/maps")
        result=[]
        for i in payload.get("list", []):
            if active_only and i.get("disabled", False): continue
            mode=i.get("gameMode")
            result.append(Map(id=i["id"], name=i["name"], image_url=i.get("imageUrl"), disabled=i.get("disabled", False), game_mode=GameMode(id=mode["id"], name=mode["name"], image_url=mode.get("imageUrl"), disabled=mode.get("disabled", False)) if mode else None))
        return result

brawl_api = BrawlApiClient()
