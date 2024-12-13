package pt.psoft.g1.psoftg1.suggestionmanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.suggestionmanagement.dbSchema.MongoSuggestionModel;
import pt.psoft.g1.psoftg1.suggestionmanagement.mapper.SuggestionMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SuggestionMongoRepoImpl implements SuggestionRepository {

    private final MongoTemplate mt;
    private final SuggestionMapper suggestionMapper;

    @Override
    public Suggestion save(Suggestion suggestion) {
        MongoSuggestionModel mongoSuggestion = suggestionMapper.toMongoSuggestionModel(suggestion);
        MongoSuggestionModel savedSuggestion = mt.save(mongoSuggestion);

        return suggestionMapper.fromMongoSuggestionModel(savedSuggestion);
    }

    @Override
    public void delete(Suggestion suggestion) {
        MongoSuggestionModel mongoSuggestion = suggestionMapper.toMongoSuggestionModel(suggestion);

        mt.remove(mongoSuggestion);
    }

    @Override
    public List<Suggestion> findAll() {
        List<MongoSuggestionModel> mongoSuggestions = mt.findAll(MongoSuggestionModel.class);

        return suggestionMapper.fromMongoSuggestionModel(mongoSuggestions);
    }

    @Override
    public Optional<Suggestion> findById(String id) {
        MongoSuggestionModel mongoSuggestion = mt.findById(id, MongoSuggestionModel.class);

        return Optional.ofNullable(mongoSuggestion)
                .map(suggestionMapper::fromMongoSuggestionModel);
    }

    @Override
    public Optional<Suggestion> findByIsbn(String isbn) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isbn").is(isbn));
        MongoSuggestionModel mongoSuggestion = mt.findOne(query, MongoSuggestionModel.class);

        return Optional.ofNullable(mongoSuggestion).map(suggestionMapper::fromMongoSuggestionModel);
    }

}
