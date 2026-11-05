from sqlalchemy import select
from sqlalchemy.orm import Session
from app.db_models import MetaSnapshotRow
from app.models.snapshots import MetaSnapshotRecord

class PersistentSnapshotRepository:
    def save(self, db: Session, record: MetaSnapshotRecord) -> MetaSnapshotRecord:
        row = MetaSnapshotRow(**record.model_dump())
        db.add(row)
        db.commit()
        return record

    def latest(self, db: Session, scope: str | None = None) -> MetaSnapshotRecord | None:
        stmt = select(MetaSnapshotRow).order_by(MetaSnapshotRow.created_at.desc())
        if scope:
            stmt = stmt.where(MetaSnapshotRow.scope == scope)
        row = db.execute(stmt.limit(1)).scalar_one_or_none()
        return MetaSnapshotRecord.model_validate(row, from_attributes=True) if row else None

persistent_snapshot_repository = PersistentSnapshotRepository()
