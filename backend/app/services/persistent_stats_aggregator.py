from collections import Counter
from datetime import datetime, timezone
from sqlalchemy.orm import Session
from app.models.competitive_context import CompetitiveContext
from app.services.battle_repository import battle_repository

class PersistentStatsAggregator:
    def aggregate(self, db: Session, context: CompetitiveContext | None = None):
        context = context or CompetitiveContext()
        samples = battle_repository.all(db)
        if context.map_id is not None:
            samples = [s for s in samples if s.map_id == context.map_id]
        if context.mode:
            samples = [s for s in samples if s.mode == context.mode]
        if context.rank:
            samples = [s for s in samples if s.rank == context.rank]
        if context.subrank:
            samples = [s for s in samples if s.subrank == context.subrank]
        if context.patch:
            samples = [s for s in samples if s.patch == context.patch]

        picks, wins = Counter(), Counter()
        for sample in samples:
            picks.update(sample.brawler_ids)
            wins.update(sample.winner_brawler_ids)

        rows = [{
            "brawler_id": str(bid),
            "picks": count,
            "pick_rate": round(count / max(1, len(samples)) * 100, 2),
            "wins": wins[bid],
            "win_rate": round(wins[bid] / max(1, count) * 100, 2),
        } for bid, count in picks.items()]
        return {
            "context": context.model_dump(),
            "scope": context.scope_key(),
            "sample_size": len(samples),
            "updated_at": datetime.now(timezone.utc).isoformat(),
            "rows": sorted(rows, key=lambda x: (x["win_rate"], x["pick_rate"]), reverse=True),
        }

persistent_stats_aggregator = PersistentStatsAggregator()
