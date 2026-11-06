from datetime import datetime, timezone

from app.db import SessionLocal
from app.models.ingestion import BattleSample, IngestionResult
from app.services.battle_repository import battle_repository
from app.services.official_api import official_api


def _normalize_tag(tag: str | None) -> str:
    return (tag or "").strip().upper().lstrip("#")


def _parse_battle_time(value: str | None) -> datetime:
    if not value:
        return datetime.now(timezone.utc)
    try:
        return datetime.strptime(value, "%Y%m%dT%H%M%S.%fZ").replace(tzinfo=timezone.utc)
    except ValueError:
        return datetime.now(timezone.utc)


def _team_brawler_ids(team: list[dict]) -> list[int]:
    return [
        member.get("brawler", {}).get("id")
        for member in team
        if member.get("brawler", {}).get("id") is not None
    ]


def _winner_brawler_ids(
    teams: list[list[dict]], result: str | None, source_player_tag: str
) -> list[int]:
    # Battle-log results are reported from the queried player's perspective.
    # Only mark the queried player's team as winners when that player won.
    if result != "victory":
        return []

    source = _normalize_tag(source_player_tag)
    for team in teams:
        if any(_normalize_tag(member.get("tag")) == source for member in team):
            return _team_brawler_ids(team)

    # If the official payload does not expose player tags, do not guess winners.
    # Returning no winners is preferable to publishing distorted win rates.
    return []


class BattleIngestionService:
    """Collects and normalizes official battle-log responses without inventing results."""

    async def collect_player(self, player_tag: str) -> IngestionResult:
        payload = await official_api.battle_log(player_tag)
        items = payload.get("items", [])
        accepted = 0

        db = SessionLocal()
        try:
            for item in items:
                battle = item.get("battle", {})
                event = item.get("event", {})
                teams = battle.get("teams", []) or []
                brawler_ids = [
                    brawler_id
                    for team in teams
                    for brawler_id in _team_brawler_ids(team)
                ]
                if not brawler_ids:
                    continue

                sample = BattleSample(
                    battle_time=_parse_battle_time(item.get("battleTime")),
                    event_id=event.get("id"),
                    map_id=event.get("map", {}).get("id"),
                    mode=event.get("mode"),
                    battle_type=battle.get("type"),
                    brawler_ids=brawler_ids,
                    winner_brawler_ids=_winner_brawler_ids(
                        teams, battle.get("result"), player_tag
                    ),
                    source_player_tag=player_tag,
                )

                if battle_repository.add(db, sample):
                    accepted += 1

            battle_repository.commit(db)
        finally:
            db.close()

        return IngestionResult(
            source="official_brawl_stars_api",
            fetched=len(items),
            accepted=accepted,
            rejected=len(items) - accepted,
            collected_at=datetime.now(timezone.utc),
        )


battle_ingestion = BattleIngestionService()
