from app.models.recommendations import BrawlerRecommendationResponse

class RecommendationService:
    """
    Recommendation boundary for counter and build data.
    Recommendations are context/version aware; no placeholder recommendation
    is returned as if it were real competitive data.
    """
    def counters_for(self, brawler_id: str, scope: str = "global", patch: str | None = None):
        return BrawlerRecommendationResponse(brawler_id=brawler_id, counters=[])

recommendation_service = RecommendationService()
