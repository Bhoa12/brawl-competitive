from datetime import datetime, timezone
from app.models.ingestion import BattleSample, IngestionResult
from app.services.official_api import official_api


class BattleIngestionService:
    """
    Normalizes official battle-log responses.

    Storage is intentionally separated from collection: the next layer can use
    PostgreSQL/Timescale without changing the API client.
    """

    def __init__(self) -> None:
        self.samples: list[BattleSample] = []

    async def collect_player(self, player_tag: str) -> IngestionResult:
        payload = await official_api.battle_log(player_tag)
        items = payload.get("items", [])
        accepted = 0

        for item in items:
            battle = item.get("battle", {})
            event = item.get("event", {})
            teams = battle.get("teams", []) or []
            brawler_ids = [
                member.get("brawler", {}).get("id")
                for team in teams
                for member in team
                if member.get("brawler", {}).get("id") is not None
            ]
            if not brawler_ids:
                continue

            result = battle.get("result")
            winner_ids = brawler_ids if result == "victory" else []
            try:
                battle_time = datetime.strptime(item["battleTime"], "%Y%m%dT%H%M%S.%fZ").replace(tzinfo=timezone.utc)
            except (KeyError, ValueError):
                battle_time = datetime.now(timezone.utc)

            self.samples.append(BattleSample(
                battle_time=battle_time,
                event_id=event.get("id"),
                map_id=event.get("map", {}).get("id"),
                mode=event.get("mode"),
                battle_type=battle.get("type"),
                brawler_ids=brawler_ids,
                winner_brawler_ids=winner_ids,
                source_player_tag=player_tag,
            ))
            accepted += 1

        return IngestionResult(
            source="official_brawl_stars_api",
            fetched=len(items),
            accepted=accepted,
            rejected=len(items) - accepted,
            collected_at=datetime.now(timezone.utc),
        )


battle_ingestion = BattleIngestionService()
