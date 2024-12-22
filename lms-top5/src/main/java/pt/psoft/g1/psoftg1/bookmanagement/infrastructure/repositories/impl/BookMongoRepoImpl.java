package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<BookCountDTO> findTopXBooksLent(Pageable pageable) {
        // Step 2: Group by book ID and count the number of lendings
        AggregationOperation groupByBookAndCount = Aggregation.group("bookId")
                .count().as("lendingCount");

        // Step 3: Sort by lending count in descending order
        SortOperation sortByLendingCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount"));

        // Step 4: Limit the results to the top X books
        AggregationOperation limit = Aggregation.limit(pageable.getPageSize());

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                groupByBookAndCount,
                sortByLendingCountDesc,
                limit
        );

        // Execute the aggregation query
        AggregationResults<Document> results = mt.aggregate(aggregation, "lendings", Document.class);

        // Step 5: Retrieve the book details and map to BookCountDTO
        List<BookCountDTO> topBooks = results.getMappedResults().stream()
                .map(doc -> {
                    String bookId = doc.getString("pk");
                    long lendingCount = doc.getLong("lendingCount");

                    MongoBookModel mongoBook = mt.findById(bookId, MongoBookModel.class);
                    Book book = bookMapper.fromMongoBookModel(mongoBook);

                    return new BookCountDTO(book, lendingCount);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(topBooks, pageable, topBooks.size());
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
