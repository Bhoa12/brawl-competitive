from app.models.competitive import BuildChoice, CompetitiveBuild, CounterRecommendation

# Temporary seed data. This repository boundary will later be backed by
# observed competitive data and versioned recommendations.
SHELLY_BUILD = CompetitiveBuild(
    brawler_id=16000000,
    brawler_name="Shelly",
    gadget=BuildChoice(id="fast-forward", name="Fast Forward"),
    star_power=BuildChoice(id="shell-shock", name="Shell Shock"),
    gear_1=BuildChoice(id="damage", name="Damage"),
    gear_2=BuildChoice(id="speed", name="Speed"),
    patch="seed",
)

COUNTERS = [
    CounterRecommendation(
        target_brawler_id=16000009,
        target_brawler_name="Mortis",
        counter_brawler_id=16000000,
        counter_brawler_name="Shelly",
        confidence=0.70,
        reason="Recomendación inicial de ejemplo; deberá sustituirse por datos observados y análisis versionado.",
        build=SHELLY_BUILD,
        patch="seed",
    )
]


class CompetitiveRepository:
    def counters_for(self, brawler_id: int) -> list[CounterRecommendation]:
        return [item for item in COUNTERS if item.target_brawler_id == brawler_id]

    def build_for(self, brawler_id: int) -> CompetitiveBuild | None:
        if brawler_id == 16000000:
            return SHELLY_BUILD
        return None


competitive_repository = CompetitiveRepository()
