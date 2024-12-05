package pt.psoft.g1.psoftg1.recommendationmanagement.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookMapper;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.dbschema.JpaRecommendationModel;
import pt.psoft.g1.psoftg1.recommendationmanagement.dbschema.MongoRecommendationModel;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;

import java.util.List;

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
}
