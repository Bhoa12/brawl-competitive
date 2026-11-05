from datetime import datetime, timezone
from uuid import uuid4
from sqlalchemy.orm import Session
from app.models.snapshots import MetaSnapshotRecord
from app.services.persistent_snapshot_repository import persistent_snapshot_repository
from app.services.persistent_stats_aggregator import persistent_stats_aggregator

class PersistentSnapshotService:
    def create(self, db: Session, *, patch: str, scope: str = "global", map_id: int | None = None, mode: str | None = None):
        stats = persistent_stats_aggregator.aggregate(db, map_id=map_id, mode=mode)
        record = MetaSnapshotRecord(
            id=str(uuid4()),
            patch=patch,
            scope=scope,
            created_at=datetime.now(timezone.utc),
            sample_size=stats["sample_size"],
            entries=stats["rows"],
        )
        return persistent_snapshot_repository.save(db, record)

persistent_snapshot_service = PersistentSnapshotService()
