package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public abstract class RecommendationViewAMQPMapper {
    public abstract RecommendationViewAMQP toRecommendationViewAMQP(String isbn, String readerNumber, boolean isPositive);
}
