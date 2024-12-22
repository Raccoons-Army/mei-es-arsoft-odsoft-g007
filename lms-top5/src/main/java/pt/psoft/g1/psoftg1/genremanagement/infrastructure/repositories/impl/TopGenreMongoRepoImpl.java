package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.genremanagement.mapper.TopGenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.TopGenre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.TopGenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TopGenreMongoRepoImpl implements TopGenreRepository {

    private final MongoTemplate mt;

    private final TopGenreMapper topGenreMapper;


    @Override
    public void deleteAll() {

    }

    @Override
    public TopGenre save(TopGenre entity) {
        return null;
    }

    @Override
    public void delete(TopGenre entity) {

    }

    @Override
    public List<TopGenre> findAll() {
        return List.of();
    }

    @Override
    public Optional<TopGenre> findById(String s) {
        return Optional.empty();
    }
}
