from datetime import datetime, timezone
from app.models.recommendations import BrawlerRecommendationResponse

# Explicitly curated baseline. These entries are tactical relationships, not
# fabricated win-rate statistics. Statistical confidence remains unavailable
# until a verified match-data provider is connected.
BASELINE_COUNTERS = {
    "Shelly": [("Spike", "Mantén la distancia: Spike puede desgastar a Shelly antes de que entre en rango."), ("Gale", "El control de Gale dificulta que Shelly cierre la distancia.")],
    "Frank": [("Colette", "El daño porcentual de Colette es especialmente efectivo contra objetivos con mucha vida."), ("Spike", "Puede desgastar a Frank y castigarlo mientras prepara sus ataques.")],
    "Mortis": [("Shelly", "El alto daño a corta distancia castiga las entradas de Mortis."), ("Gale", "El control de masas puede interrumpir y alejar a Mortis.")],
}

class RecommendationService:
    def counters_for(self, brawler_id: str, scope: str = "global", patch: str | None = None):
        entries = BASELINE_COUNTERS.get(brawler_id, [])
        counters = [
            {
                "name": name,
                "reason": reason,
                "evidence": "Análisis táctico curado",
                "statistics_available": False,
            }
            for name, reason in entries
        ]
        return {
            "brawler_id": brawler_id,
            "scope": scope,
            "patch": patch,
            "updated_at": datetime.now(timezone.utc).isoformat(),
            "counters": counters,
            "statistics_available": False,
            "notice": "Las recomendaciones tácticas no representan win rates hasta disponer de una muestra verificable.",
        }

recommendation_service = RecommendationService()
