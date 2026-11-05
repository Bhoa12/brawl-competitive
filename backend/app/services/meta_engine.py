from datetime import datetime, timezone

from app.models.meta import MetaMetric, MetaSnapshot


class MetaEngine:
    """
    Calculates a normalized score when observed statistics are available.

    This initial implementation intentionally returns an empty snapshot instead
    of inventing current statistics. A collector/database will populate the
    engine with observed samples.
    """

    def snapshot(
        self,
        *,
        map_id: int | None = None,
        mode_id: int | None = None,
        rank: str | None = None,
        subrank: str | None = None,
        patch: str = "unknown",
    ) -> MetaSnapshot:
        now = datetime.now(timezone.utc)
        scope_parts = [f"map={map_id}" if map_id else "all-maps"]
        if mode_id: scope_parts.append(f"mode={mode_id}")
        if rank: scope_parts.append(f"rank={rank}")
        if subrank: scope_parts.append(f"subrank={subrank}")
        return MetaSnapshot(
            scope=" | ".join(scope_parts),
            updated_at=now,
            patch=patch,
            sample_size=0,
            metrics=[],
        )

    @staticmethod
    def score(
        *,
        win_rate: float,
        pick_rate: float,
        trend: float = 0,
        matchup_score: float = 50,
    ) -> float:
        """
        Deterministic scoring formula. Inputs are percentages except trend,
        which is normalized to [-1, 1].
        """
        win_component = max(0, min(100, (win_rate - 40) * 5))
        pick_component = max(0, min(100, pick_rate * 10))
        trend_component = max(0, min(100, 50 + trend * 50))
        return round(
            0.50 * win_component
            + 0.20 * pick_component
            + 0.15 * trend_component
            + 0.15 * max(0, min(100, matchup_score)),
            2,
        )


meta_engine = MetaEngine()
