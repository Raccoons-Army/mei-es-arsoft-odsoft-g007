package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.*;

@RequiredArgsConstructor
public class GenreMongoRepoImpl implements GenreRepository {

    private final MongoTemplate mt;
    private final GenreMapper genreMapper;

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        // Step 1: Group by genre and count the number of books in each genre
        AggregationOperation groupByGenreAndCount = Aggregation.group("genre")
                .count().as("bookCount");

        // Step 2: Sort by book count in descending order
        SortOperation sortByBookCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "bookCount"));

        // Step 3: Pagination using skip and limit
        AggregationOperation skip = Aggregation.skip(pageable.getOffset());
        AggregationOperation limit = Aggregation.limit(pageable.getPageSize());

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                groupByGenreAndCount,
                sortByBookCountDesc,
                skip,
                limit
        );

        // Execute the aggregation query
        AggregationResults<GenreBookCountDTO> results = mt.aggregate(aggregation, "books", GenreBookCountDTO.class);

        // Step 4: Calculate total genre count for pagination
        long total = mt.count(new Query(), "books");

        // Return a Page object containing the results and pagination details
        return new PageImpl<>(results.getMappedResults(), pageable, total);
    }

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
