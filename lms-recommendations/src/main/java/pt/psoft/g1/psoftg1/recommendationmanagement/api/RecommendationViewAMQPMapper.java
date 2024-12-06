package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

@Mapper(componentModel = "spring")
public abstract class RecommendationViewAMQPMapper {
    @Mapping(target = "readerNumber", expression = "java(recommendation.getReaderDetails().getReaderNumber())")
    @Mapping(target = "isbn", expression = "java(recommendation.getBook().getIsbn())")
    @Mapping(target = "positive", source = "positive")
    @Mapping(target = "version", source = "version")
    public abstract RecommendationViewAMQP toRecommendationViewAMQP(Recommendation recommendation);
}
