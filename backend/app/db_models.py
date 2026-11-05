from datetime import datetime, timezone
from sqlalchemy import DateTime, Float, Integer, JSON, String
from sqlalchemy.orm import Mapped, mapped_column
from app.db import Base

class MetaSnapshotRow(Base):
    __tablename__ = "meta_snapshots"
    id: Mapped[str] = mapped_column(String(64), primary_key=True)
    patch: Mapped[str] = mapped_column(String(64), index=True)
    scope: Mapped[str] = mapped_column(String(255), index=True)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), index=True)
    sample_size: Mapped[int] = mapped_column(Integer, default=0)
    entries: Mapped[list] = mapped_column(JSON, default=list)

class BattleSampleRow(Base):
    __tablename__ = "battle_samples"
    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    battle_time: Mapped[datetime] = mapped_column(DateTime(timezone=True), index=True)
    map_id: Mapped[int | None] = mapped_column(Integer, nullable=True, index=True)
    mode: Mapped[str | None] = mapped_column(String(64), nullable=True, index=True)
    brawler_ids: Mapped[list] = mapped_column(JSON, default=list)
    winner_brawler_ids: Mapped[list] = mapped_column(JSON, default=list)
