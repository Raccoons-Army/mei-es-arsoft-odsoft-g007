package pt.psoft.g1.psoftg1.recommendationmanagement.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookMapper;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.recommendationmanagement.dbschema.JpaRecommendationModel;
import pt.psoft.g1.psoftg1.recommendationmanagement.dbschema.MongoRecommendationModel;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationDTO;
import pt.psoft.g1.psoftg1.shared.utils.DateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ReaderDetailsMapper.class, BookMapper.class})
public abstract class RecommendationMapper {

    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "isPositive", source = "positive")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract Recommendation fromMongoRecommendationModel(MongoRecommendationModel mongoRecommendationModel);
    public abstract List<Recommendation> fromMongoRecommendationModel(List<MongoRecommendationModel> mongoRecommendationModel);

    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "positive", source = "positive")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract MongoRecommendationModel toMongoRecommendationModel(Recommendation recommendation);

    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "isPositive", source = "positive")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract Recommendation fromJpaRecommendationModel(JpaRecommendationModel jpaRecommendationModel);
    public abstract List<Recommendation> fromJpaRecommendationModel(List<JpaRecommendationModel> jpaRecommendationModel);

    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "positive", source = "positive")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract JpaRecommendationModel toJpaRecommendationModel(Recommendation recommendation);

    @Mapping(target = "readerNumber", expression = "java(recommendation.getReaderDetails().getReaderNumber())")
    @Mapping(target = "isbn", expression = "java(recommendation.getBook().getIsbn())")
    @Mapping(target = "positive", source = "positive")
    @Mapping(target = "createdAt", expression = "java(formatLocalDate(recommendation.getCreatedAt()))")
    public abstract RecommendationDTO toDto(Recommendation recommendation);

    public abstract List<RecommendationDTO> toDto(List<Recommendation> recommendation);

    protected String formatLocalDate(LocalDate date) {
        return date != null ? DateUtils.ISO_DATE_FORMATTER.format(date) : null;
    }
}
