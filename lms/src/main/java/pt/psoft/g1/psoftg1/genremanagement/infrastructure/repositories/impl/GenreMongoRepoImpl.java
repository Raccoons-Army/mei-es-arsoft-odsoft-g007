package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import org.bson.Document;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GenreMongoRepoImpl implements GenreRepository {

    private final MongoTemplate mt;
    private final GenreMapper genreMapper;

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        GroupOperation groupByGenre = Aggregation.group("genre")
                .count().as("bookCount");
        Aggregation aggregation = Aggregation.newAggregation(
                groupByGenre,
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "bookCount")),
                Aggregation.limit(5)
        );

        AggregationResults<GenreBookCountDTO> results = mt.aggregate(
                aggregation, "books", GenreBookCountDTO.class);

        List<GenreBookCountDTO> list = results.getMappedResults();
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());

        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("startDate").gte(startOfMonth).lte(endOfMonth)
        );

        GroupOperation groupByGenre = Aggregation.group("book.genre")
                .count().as("loanCount")
                .avg("loanCount").as("dailyAvgLoans");

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupByGenre);
        AggregationResults<GenreLendingsDTO> results = mt.aggregate(
                aggregation, "lendings", GenreLendingsDTO.class);

        return results.getMappedResults().stream()
                .skip((page.getNumber() - 1) * page.getLimit())
                .limit(page.getLimit())
                .collect(Collectors.toList());
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        return null;
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        MatchOperation matchOperation = Aggregation.match(
                Criteria.where("startDate").gte(startDate).lte(endDate).and("returnedDate").exists(true)
        );

        // Group by genre and calculate the average lending duration
        GroupOperation groupByGenreAndMonth = Aggregation.group("book.genre")
                .first("startDate").as("startDate")  // Store startDate for later use
                .avg(AggregationExpression.from(MongoExpression.create(
                        "{ $divide: [{ $subtract: ['$returnedDate', '$startDate'] }, 86400000] }"
                ))).as("averageDuration");  // Average duration in days

        // Project the necessary fields: year, month, genre, and averageDuration
        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression("year(startDate)").as("year")
                .andExpression("month(startDate)").as("month")
                .and("averageDuration").as("averageDuration")
                .and("_id").as("genre");

        // Combine the aggregation stages
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                groupByGenreAndMonth,
                projectionOperation
        );

        // Perform the aggregation
        AggregationResults<Document> results = mt.aggregate(aggregation, "lendings", Document.class);

        // Collecting results into the desired map structure
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults = results.getMappedResults().stream()
                .collect(Collectors.groupingBy(
                        result -> result.getInteger("year"),  // Retrieve year as Integer
                        Collectors.groupingBy(result -> result.getInteger("month"),  // Retrieve month as Integer
                                Collectors.mapping(result -> new GenreLendingsDTO(
                                        result.getString("genre"),  // Retrieve genre as String
                                        result.getDouble("averageDuration")  // Retrieve averageDuration as Double
                                ), Collectors.toList()))
                ));

        return getGenreLendingsPerMonthDtos(groupedResults);
    }

    @Override
    public List<Genre> findAllGenres() {
        return mt.findAll(Genre.class);
    }

    @Override
    public Optional<Genre> findReaderMostRequestedGenre(String readerNumber) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("readerDetails.readerNumber").is(readerNumber));
        GroupOperation groupByGenre = Aggregation.group("book.genre").count().as("lendingCount");
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation, groupByGenre, Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")), Aggregation.limit(1)
        );

        AggregationResults<MongoGenreModel> results = mt.aggregate(aggregation, "lendings", MongoGenreModel.class);
        Optional<MongoGenreModel> list = Optional.ofNullable(results.getUniqueMappedResult());

        return list.map(genreMapper::fromMongoGenre);
    }

    @Override
    public List<Genre> getTopYGenresMostLent(int y) {
        GroupOperation groupByGenre = Aggregation.group("book.genre").count().as("lendingCount");
        Aggregation aggregation = Aggregation.newAggregation(
                groupByGenre, Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")), Aggregation.limit(y)
        );

        AggregationResults<MongoGenreModel> results = mt.aggregate(aggregation, "lendings", MongoGenreModel.class);
        List<MongoGenreModel> list = results.getMappedResults();

        return genreMapper.fromMongoGenre(list);
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

    @NotNull
    private List<GenreLendingsPerMonthDTO> getGenreLendingsPerMonthDtos(Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults) {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, List<GenreLendingsDTO>>> yearEntry : groupedResults.entrySet()) {
            int yearValue = yearEntry.getKey();
            for (Map.Entry<Integer, List<GenreLendingsDTO>> monthEntry : yearEntry.getValue().entrySet()) {
                int monthValue = monthEntry.getKey();
                List<GenreLendingsDTO> values = monthEntry.getValue();
                lendingsPerMonth.add(new GenreLendingsPerMonthDTO(yearValue, monthValue, values));
            }
        }

        return lendingsPerMonth;
    }
}
