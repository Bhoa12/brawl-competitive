from app.services.snapshot_repository import snapshot_repository
from app.services.stats_aggregator import stats_aggregator

class SnapshotService:
    def create(self, patch: str, scope: str = "global"):
        stats = stats_aggregator.aggregate()
        return snapshot_repository.save(
            patch=patch,
            scope=scope,
            sample_size=stats["sample_size"],
            entries=stats["rows"],
        )

    def compare_latest(self, scope: str = "global"):
        records = list(reversed(snapshot_repository._records))
        scoped = [r for r in records if r.scope == scope]
        if len(scoped) < 2:
            return {"current": scoped[0].model_dump() if scoped else None, "previous": None, "changes": []}
        current, previous = scoped[0], scoped[1]
        previous_by_id = {e["brawler_id"]: e for e in previous.entries}
        changes = []
        for entry in current.entries:
            old = previous_by_id.get(entry["brawler_id"])
            if old:
                changes.append({
                    "brawler_id": entry["brawler_id"],
                    "pick_rate_change": round(entry["pick_rate"] - old["pick_rate"], 2),
                    "win_rate_change": round(entry["win_rate"] - old["win_rate"], 2),
                })
        return {"current": current.model_dump(), "previous": previous.model_dump(), "changes": changes}

snapshot_service = SnapshotService()
