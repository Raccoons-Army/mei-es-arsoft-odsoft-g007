package pt.psoft.g1.psoftg1.recommendationmanagement.publishers;

public interface RecommendationEventPublisher {
        String sendRecommendationCreated(String isbn, String readerNumber, boolean isPositive);
}
