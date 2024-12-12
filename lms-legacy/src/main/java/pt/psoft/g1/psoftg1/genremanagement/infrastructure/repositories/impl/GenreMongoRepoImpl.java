package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import com.mongodb.DBRef;
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
import java.util.*;
import java.util.stream.Collectors;

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
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        int days = month.lengthOfMonth();
        LocalDate firstOfMonth = LocalDate.of(month.getYear(), month.getMonth(), 1);
        LocalDate lastOfMonth = LocalDate.of(month.getYear(), month.getMonth(), days);

        // Step 1: Filter lendings within the specified month
        MatchOperation matchByDate = Aggregation.match(Criteria.where("startDate")
                .gte(firstOfMonth)
                .lte(lastOfMonth));

        // Step 2: Group by genre and count lendings
        AggregationOperation groupByGenreAndCount = Aggregation.group("book.genre")
                .count().as("totalLendings");

        // Step 3: Project to calculate daily average lendings for each genre
        ProjectionOperation calculateDailyAvg = Aggregation.project("totalLendings")
                .and("totalLendings").divide(days).as("averageLendings")
                .and("_id").as("genre");

        // Step 4: Pagination using skip and limit
        AggregationOperation skip = Aggregation.skip((long) (page.getNumber() - 1) * page.getLimit());
        AggregationOperation limit = Aggregation.limit(page.getLimit());

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                matchByDate,
                groupByGenreAndCount,
                calculateDailyAvg,
                skip,
                limit
        );

        // Execute the aggregation query
        AggregationResults<GenreLendingsDTO> results = mt.aggregate(aggregation, "lendings", GenreLendingsDTO.class);

        // Return the list of GenreLendingsDTO results
        return results.getMappedResults();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        // Step 1: Define date range for the last 12 months
        LocalDate now = LocalDate.now();
        LocalDate twelveMonthsAgo = now.minusMonths(12);

        // Step 2: Match lendings within the last 12 months
        MatchOperation matchLastYear = Aggregation.match(Criteria.where("startDate").gte(twelveMonthsAgo).lte(now));

        // Step 3: Project genre, year, and month
        ProjectionOperation projectFields = Aggregation.project()
                .and("book.genre").as("genre")
                .andExpression("year(startDate)").as("year")
                .andExpression("month(startDate)").as("month");

        // Step 4: Group by genre, year, and month, and count lendings
        AggregationOperation groupByGenreYearMonth = Aggregation.group("genre", "year", "month")
                .count().as("value");

        // Step 5: Sort by year, month, and genre
        SortOperation sortByYearMonthGenre = Aggregation.sort(Sort.by(Sort.Direction.ASC, "year", "month", "genre"));

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                matchLastYear,
                projectFields,
                groupByGenreYearMonth,
                sortByYearMonthGenre
        );

        // Execute the aggregation query
        AggregationResults<Map> results = mt.aggregate(aggregation, "lendings", Map.class);

        // Process results to group by year and month
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults = new HashMap<>();

        for (Map result : results.getMappedResults()) {
            String genre = (String) result.get("_id.genre");
            int year = (Integer) result.get("_id.year");
            int month = (Integer) result.get("_id.month");
            Number count = (Number) result.get("value");

            GenreLendingsDTO genreLendingsDTO = new GenreLendingsDTO(genre, count);

            groupedResults
                    .computeIfAbsent(year, k -> new HashMap<>())
                    .computeIfAbsent(month, k -> new ArrayList<>())
                    .add(genreLendingsDTO);
        }

        // Convert grouped results to GenreLendingsPerMonthDTO
        return getGenreLendingsPerMonthDtos(groupedResults);
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
        List<MongoGenreModel> list = mt.findAll(MongoGenreModel.class);
        return genreMapper.fromMongoGenre(list);
    }

    @Override
    public Optional<Genre> findReaderMostRequestedGenre(String readerNumber) {
        // Perform a lookup to join the readerDetails collection with the lendings collection
        AggregationOperation lookupReaderDetails = Aggregation.lookup("readersDetails", "readerDetails.$id", "_id", "readerDetailsResolved");

        // Unwind the readerDetailsResolved array to access the readerDetails field
        AggregationOperation unwindReaderDetails = Aggregation.unwind("readerDetailsResolved");

        // Match the lendings based on the readerNumber field inside readerDetails
        MatchOperation matchOperation = Aggregation.match(Criteria.where("readerDetailsResolved.readerNumber").is(readerNumber));

        // Lookup to resolve the genre from the books collection
        AggregationOperation lookupGenre = Aggregation.lookup("books", "book.$id", "_id", "bookDetails");

        // Unwind the bookDetails array (to make genre accessible)
        AggregationOperation unwindBookDetails = Aggregation.unwind("bookDetails");

        // Group by the book genre (after resolving the genre)
        GroupOperation groupByGenre = Aggregation.group("bookDetails.genre").count().as("lendingCount");

        // Sort by the number of lendings in descending order
        SortOperation sortByLendingCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount"));

        // Limit the result to 1 (most requested genre)
        AggregationOperation limit = Aggregation.limit(1);

        // Combine all operations into the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                lookupReaderDetails,
                unwindReaderDetails,
                matchOperation,
                lookupGenre,
                unwindBookDetails,
                groupByGenre,
                sortByLendingCountDesc,
                limit
        );

        // Execute the aggregation query
        AggregationResults<Document> results = mt.aggregate(aggregation, "lendings", Document.class);

        // Get the first result (if available)
        Optional<Document> result = Optional.ofNullable(results.getUniqueMappedResult());

        // Map the result to MongoGenreModel and convert it to Genre
        return result.map(doc -> {
            // Extract the genre reference from _id (which contains $ref and $id)
            DBRef genreRef = (DBRef) doc.get("_id");  // The _id field contains the DBRef
            String genreId = genreRef.getId().toString();  // Get the actual ObjectId from DBRef

            // Retrieve the MongoGenreModel using the genreId
            MongoGenreModel genre = mt.findById(genreId, MongoGenreModel.class);

            // Convert to Genre model (assuming you have a genreMapper)
            return genreMapper.fromMongoGenre(genre);
        });
    }


    @Override
    public List<Genre> getTopYGenresMostLent(int y) {
        // First, perform a lookup to join the genre from the genres collection
        AggregationOperation lookupBooks = Aggregation.lookup("books", "book.$id", "_id", "bookDetails");

        // Group by the genre after resolving the reference
        GroupOperation groupByGenre = Aggregation.group("bookDetails.genre")
                .count().as("lendingCount");

        // Create the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                lookupBooks,
                groupByGenre,
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")),
                Aggregation.limit(y)
        );

        // Execute the aggregation
        AggregationResults<Map> results = mt.aggregate(aggregation, "lendings", Map.class);

        // Map the results into your Genre list
        List<Map> resultList = results.getMappedResults();
        List<Genre> genres = new ArrayList<>();

        for (Map result : resultList) {
            // Extract genre from the ArrayList (bookDetails.genre)
            List<?> genreList = (List<?>) result.get("_id"); // _id holds the genre array from lookup
            if (genreList != null && !genreList.isEmpty()) {
                MongoGenreModel genre = (MongoGenreModel) genreList.get(0); // Get the first genre in the list
                genres.add(genreMapper.fromMongoGenre(genre));
            }
        }

        return genres;
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
