package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.authormanagement.mapper.TopAuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.TopAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.TopAuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TopAuthorMongoRepoImpl implements TopAuthorRepository {

    private final MongoTemplate mt;

    private final TopAuthorMapper topAuthorMapper;


    @Override
    public void deleteAll() {

    }

    @Override
    public TopAuthor save(TopAuthor entity) {
        return null;
    }

    @Override
    public void delete(TopAuthor entity) {

    }

    @Override
    public List<TopAuthor> findAll() {
        return List.of();
    }

    @Override
    public Optional<TopAuthor> findById(String s) {
        return Optional.empty();
    }
}
