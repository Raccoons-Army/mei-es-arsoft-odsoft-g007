package pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import pt.psoft.g1.psoftg1.recommendationmanagement.dbschema.MongoRecommendationModel;
import pt.psoft.g1.psoftg1.recommendationmanagement.mapper.RecommendationMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecommendationMongoRepoImpl implements RecommendationRepository {

    private final MongoTemplate mt;
    private final RecommendationMapper recommendationMapper;

    @Override
    public Optional<Recommendation> findByBookIsbnAndReaderNumber(String bookIsbn, String readerNumber) {
        AggregationOperation lookupBook = Aggregation.lookup("book", "book.$id", "_id", "bookDetails");
        AggregationOperation lookupReader = Aggregation.lookup("readerDetails", "readerDetails.$id", "_id", "reader");
        MatchOperation match = Aggregation.match(Criteria.where("bookDetails.isbn").is(bookIsbn)
                .and("readerDetails.readerNumber").is(readerNumber));

        AggregationOperation projectFields = Aggregation.project("pk", "book", "readerDetails", "positive", "createdAt", "version");
        Aggregation aggregation = Aggregation.newAggregation(lookupBook, lookupReader, match, projectFields);

        AggregationResults<MongoRecommendationModel> results = mt.aggregate(aggregation, "recommendation", MongoRecommendationModel.class);

        return Optional.ofNullable(recommendationMapper.fromMongoRecommendationModel(results.getUniqueMappedResult()));
    }

    @Override
    public Recommendation save(Recommendation entity) {
        MongoRecommendationModel mongoRecommendationModel = recommendationMapper.toMongoRecommendationModel(entity);
        MongoRecommendationModel savedRecommendation = mt.save(mongoRecommendationModel);

        return recommendationMapper.fromMongoRecommendationModel(savedRecommendation);
    }

    @Override
    public void delete(Recommendation entity) {
        MongoRecommendationModel mongoRecommendationModel = recommendationMapper.toMongoRecommendationModel(entity);
        mt.remove(mongoRecommendationModel);
    }

    @Override
    public List<Recommendation> findAll() {
        return List.of();
    }

    @Override
    public Optional<Recommendation> findById(Long aLong) {
        return Optional.empty();
    }
}
