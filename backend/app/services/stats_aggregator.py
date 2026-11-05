from collections import Counter
from datetime import datetime, timezone
from app.services.battle_ingestion import battle_ingestion

class StatsAggregator:
    def aggregate(self):
        picks = Counter()
        wins = Counter()
        total = len(battle_ingestion.samples)
        for sample in battle_ingestion.samples:
            for brawler_id in sample.brawler_ids:
                picks[brawler_id] += 1
            for brawler_id in sample.winner_brawler_ids:
                wins[brawler_id] += 1
        rows = []
        for brawler_id, count in picks.items():
            rows.append({
                "brawler_id": brawler_id,
                "picks": count,
                "pick_rate": round(count / max(1, total) * 100, 2),
                "wins": wins[brawler_id],
                "win_rate": round(wins[brawler_id] / max(1, count) * 100, 2),
            })
        return {"sample_size": total, "updated_at": datetime.now(timezone.utc).isoformat(), "rows": sorted(rows, key=lambda x: x["pick_rate"], reverse=True)}

stats_aggregator = StatsAggregator()
