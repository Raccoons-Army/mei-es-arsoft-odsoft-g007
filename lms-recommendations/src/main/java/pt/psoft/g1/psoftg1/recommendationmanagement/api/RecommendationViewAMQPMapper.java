package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RecommendationViewAMQPMapper {
    @Mapping(target = "readerNumber", expression = "java(recommendation.getReaderDetails().getReaderNumber())")
    @Mapping(target = "isbn", expression = "java(recommendation.getBook().getIsbn())")
    @Mapping(target = "positive", source = "positive")
    @Mapping(target = "version", source = "version")
    public abstract RecommendationViewAMQP toRecommendationViewAMQP(Recommendation recommendation);
    public abstract  List<RecommendationViewAMQP> toRecommendationViewAMQP(List<Recommendation> recommendationList);

    @Mapping(target = "readerDetails.readerNumber", source = "readerNumber")
    @Mapping(target = "book.isbn", source = "isbn")
    @Mapping(target = "positive", source = "positive")
    @Mapping(target = "version", source = "version")
    public abstract Recommendation toRecommendation(RecommendationViewAMQP recommendationViewAMQP);
    public abstract List<Recommendation> toRecommendation(List<RecommendationViewAMQP> recommendationViewAMQPList);
}
