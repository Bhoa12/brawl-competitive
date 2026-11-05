from collections import Counter
from datetime import datetime, timezone
from sqlalchemy.orm import Session
from app.services.battle_repository import battle_repository

class PersistentStatsAggregator:
    def aggregate(self, db: Session, *, map_id: int | None = None, mode: str | None = None):
        samples = battle_repository.all(db)
        if map_id is not None:
            samples = [s for s in samples if s.map_id == map_id]
        if mode is not None:
            samples = [s for s in samples if s.mode == mode]

        picks, wins = Counter(), Counter()
        for sample in samples:
            picks.update(sample.brawler_ids)
            wins.update(sample.winner_brawler_ids)

        rows = [{
            "brawler_id": bid,
            "picks": count,
            "pick_rate": round(count / max(1, len(samples)) * 100, 2),
            "wins": wins[bid],
            "win_rate": round(wins[bid] / max(1, count) * 100, 2),
        } for bid, count in picks.items()]

        return {
            "sample_size": len(samples),
            "updated_at": datetime.now(timezone.utc).isoformat(),
            "rows": sorted(rows, key=lambda x: (x["win_rate"], x["pick_rate"]), reverse=True),
        }

persistent_stats_aggregator = PersistentStatsAggregator()
