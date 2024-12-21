package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookMongoRepoImpl implements BookRepository {

    private final MongoTemplate mt;

    private final BookMapper bookMapper;

    @Override
    public List<Book> findByGenre(String genreName) {
        // Step 1: Define the Aggregation pipeline
        AggregationOperation lookupGenre = Aggregation.lookup("genres", "genre.$id", "_id", "genreDetails");

        // Step 2: Match the genre by name in the genreDetails field (after lookup)
        MatchOperation matchGenre = Aggregation.match(Criteria.where("genreDetails.genre").is(genreName));

        // Step 3: Optional - project the necessary fields if needed
        AggregationOperation projectFields = Aggregation.project("pk", "isbn", "title", "description", "genre", "authors", "photo", "version");

        // Combine all steps in the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                lookupGenre,      // Lookup genres collection
                matchGenre,       // Match the genre name
                projectFields     // Optional: Project only necessary fields (optional)
        );

        // Execute the aggregation query
        AggregationResults<MongoBookModel> results = mt.aggregate(aggregation, "books", MongoBookModel.class);

        // Return the mapped list of books
        List<MongoBookModel> books = results.getMappedResults();

        // Map the results to your Book list
        return bookMapper.fromMongoBookModel(books);
    }
    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.authorNumber").is(authorNumber)); // Find books by author number
        List<MongoBookModel> list = mt.find(query, MongoBookModel.class);
        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public Book save(Book book) {
        MongoBookModel mongoBook = bookMapper.toMongoBookModel(book);
        MongoBookModel savedBook = mt.save(mongoBook);
        return bookMapper.fromMongoBookModel(savedBook);
    }

    @Override
    public void delete(Book book) {
        MongoBookModel mongoBook = bookMapper.toMongoBookModel(book);
        mt.remove(mongoBook);
    }

    @Override
    public List<Book> findAll() {
        List<MongoBookModel> mongoBooks = mt.findAll(MongoBookModel.class);
        return bookMapper.fromMongoBookModel(mongoBooks);
    }

    @Override
    public Optional<Book> findById(String bookId) {
        MongoBookModel mongoBook = mt.findById(bookId, MongoBookModel.class);

        return Optional.ofNullable(mongoBook)
                .map(bookMapper::fromMongoBookModel);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isbn").is(isbn));
        MongoBookModel mongoBook = mt.findOne(query, MongoBookModel.class);

        return Optional.ofNullable(mongoBook)
                .map(bookMapper::fromMongoBookModel);
    }
}
