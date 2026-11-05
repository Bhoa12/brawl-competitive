from datetime import datetime
from pydantic import BaseModel, Field

class MetaSnapshotRecord(BaseModel):
    id: str
    patch: str
    scope: str
    created_at: datetime
    sample_size: int = Field(ge=0)
    entries: list[dict] = Field(default_factory=list)
