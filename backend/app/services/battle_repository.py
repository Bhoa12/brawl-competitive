from sqlalchemy import select
from sqlalchemy.orm import Session
from app.db_models import BattleSampleRow
from app.models.ingestion import BattleSample


class BattleRepository:
    def exists(self, db: Session, sample: BattleSample) -> bool:
        candidates = db.execute(
            select(BattleSampleRow).where(BattleSampleRow.battle_time == sample.battle_time)
        ).scalars().all()
        return any(
            row.map_id == sample.map_id
            and row.mode == sample.mode
            and (row.brawler_ids or []) == sample.brawler_ids
            and (row.winner_brawler_ids or []) == sample.winner_brawler_ids
            for row in candidates
        )

    def add(self, db: Session, sample: BattleSample) -> bool:
        if self.exists(db, sample):
            return False
        db.add(BattleSampleRow(
            battle_time=sample.battle_time,
            map_id=sample.map_id,
            mode=sample.mode,
            rank=getattr(sample, "rank", None),
            subrank=getattr(sample, "subrank", None),
            patch=getattr(sample, "patch", None),
            brawler_ids=sample.brawler_ids,
            winner_brawler_ids=sample.winner_brawler_ids,
        ))
        return True

    def commit(self, db: Session) -> None:
        db.commit()

    def all(self, db: Session) -> list[BattleSample]:
        rows = db.execute(select(BattleSampleRow)).scalars().all()
        return [BattleSample(
            battle_time=r.battle_time,
            map_id=r.map_id,
            mode=r.mode,
            rank=r.rank,
            subrank=r.subrank,
            patch=r.patch,
            brawler_ids=r.brawler_ids or [],
            winner_brawler_ids=r.winner_brawler_ids or [],
        ) for r in rows]


battle_repository = BattleRepository()
