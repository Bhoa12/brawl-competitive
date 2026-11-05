from datetime import datetime, timezone
from uuid import uuid4
from app.models.snapshots import MetaSnapshotRecord

class SnapshotRepository:
    """
    Repository boundary for historical meta snapshots.
    The in-memory implementation is intentionally replaceable by PostgreSQL.
    """
    def __init__(self):
        self._records: list[MetaSnapshotRecord] = []

    def save(self, *, patch: str, scope: str, sample_size: int, entries: list[dict]) -> MetaSnapshotRecord:
        record = MetaSnapshotRecord(
            id=str(uuid4()),
            patch=patch,
            scope=scope,
            created_at=datetime.now(timezone.utc),
            sample_size=sample_size,
            entries=entries,
        )
        self._records.append(record)
        return record

    def latest(self, scope: str | None = None) -> MetaSnapshotRecord | None:
        records = [r for r in self._records if scope is None or r.scope == scope]
        return records[-1] if records else None

    def history(self, limit: int = 20) -> list[MetaSnapshotRecord]:
        return list(reversed(self._records[-limit:]))

snapshot_repository = SnapshotRepository()
