package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;

public interface RecommendationService {
    void createThroughRPC(RecommendationViewAMQP resource);
    void create(RecommendationViewAMQP resource);
    void update(RecommendationViewAMQP resource);
}
