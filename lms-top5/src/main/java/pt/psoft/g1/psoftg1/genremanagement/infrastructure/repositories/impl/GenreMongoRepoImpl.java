package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreCountDTO;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public class GenreMongoRepoImpl implements GenreRepository {

    private final MongoTemplate mt;
    private final GenreMapper genreMapper;

    @Override
    public Genre save(Genre genre) {
        MongoGenreModel mongoGenre = genreMapper.toMongoGenre(genre);
        MongoGenreModel savedGenre = mt.save(mongoGenre);

        return genreMapper.fromMongoGenre(savedGenre);
    }

    @Override
    public void delete(Genre genre) {
        MongoGenreModel mongoGenre = genreMapper.toMongoGenre(genre);

        mt.remove(mongoGenre);
    }

    @Override
    public Page<GenreCountDTO> findTopXGenreByLendings(Pageable pageable) {
        return null;
    }

    @Override
    public List<Genre> findAll() {
        List<MongoGenreModel> mongoGenres = mt.findAll(MongoGenreModel.class);
        return genreMapper.fromMongoGenre(mongoGenres);
    }

    @Override
    public Optional<Genre> findById(String id) {
        MongoGenreModel mongoGenre = mt.findById(id, MongoGenreModel.class);

        return Optional.ofNullable(mongoGenre)
                .map(genreMapper::fromMongoGenre);
    }

    @Override
    public Optional<Genre> findByString(String genreString) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre").is(genreString));
        MongoGenreModel mongoGenre = mt.findOne(query, MongoGenreModel.class);

        return Optional.ofNullable(mongoGenre)
                .map(genreMapper::fromMongoGenre);
    }
}
