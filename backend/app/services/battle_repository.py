from sqlalchemy import select
from sqlalchemy.orm import Session
from app.db_models import BattleSampleRow
from app.models.ingestion import BattleSample

class BattleRepository:
    def add(self, db: Session, sample: BattleSample) -> None:
        db.add(BattleSampleRow(
            battle_time=sample.battle_time,
            map_id=sample.map_id,
            mode=sample.mode,
            rank=getattr(sample, 'rank', None),
            subrank=getattr(sample, 'subrank', None),
            patch=getattr(sample, 'patch', None),
            brawler_ids=sample.brawler_ids,
            winner_brawler_ids=sample.winner_brawler_ids,
        ))

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
