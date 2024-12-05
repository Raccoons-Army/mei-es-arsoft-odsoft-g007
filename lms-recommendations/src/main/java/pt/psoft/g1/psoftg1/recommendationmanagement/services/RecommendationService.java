package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;

public interface RecommendationService {
    boolean create(RecommendationViewAMQP resource);
}
