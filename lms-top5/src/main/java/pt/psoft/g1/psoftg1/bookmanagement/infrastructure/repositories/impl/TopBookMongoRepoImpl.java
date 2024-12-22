package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.TopBookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.TopBookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TopBookMongoRepoImpl implements TopBookRepository {

    private final MongoTemplate mt;

    private final TopBookMapper topBookMapper;


    @Override
    public void deleteAll() {

    }

    @Override
    public TopBook save(TopBook entity) {
        return null;
    }

    @Override
    public void delete(TopBook entity) {

    }

    @Override
    public List<TopBook> findAll() {
        return List.of();
    }

    @Override
    public Optional<TopBook> findById(String s) {
        return Optional.empty();
    }
}
