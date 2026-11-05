from pydantic import BaseModel

class CompetitiveContext(BaseModel):
    map_id: int | None = None
    mode: str | None = None
    rank: str | None = None
    subrank: str | None = None
    patch: str | None = None

    def scope_key(self) -> str:
        parts = []
        if self.map_id is not None: parts.append(f"map:{self.map_id}")
        if self.mode: parts.append(f"mode:{self.mode}")
        if self.rank: parts.append(f"rank:{self.rank}")
        if self.subrank: parts.append(f"subrank:{self.subrank}")
        if self.patch: parts.append(f"patch:{self.patch}")
        return "|".join(parts) or "global"
