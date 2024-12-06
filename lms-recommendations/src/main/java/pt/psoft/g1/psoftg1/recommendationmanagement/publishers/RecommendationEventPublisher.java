package pt.psoft.g1.psoftg1.recommendationmanagement.publishers;

import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

public interface RecommendationEventPublisher {
    void sendRecommendationCreated(Recommendation recommendation);
    void sendRecommendationUpdated(Recommendation recommendation, Long currentVersion);
}
