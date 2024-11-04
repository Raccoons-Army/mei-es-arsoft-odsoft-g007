package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookMongoRepoImpl implements BookRepository {

    private final MongoTemplate mt;

    private final BookMapper bookMapper;

    @Override
    public List<Book> findByGenre(String genre) {
        Query query = new Query();
        query.addCriteria(Criteria.where("genre").is(genre)); // Accessing nested property
        List<MongoBookModel> m = mt.find(query, MongoBookModel.class);

        return bookMapper.fromMongoBookModel(m);
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title)); // Accessing nested property
        List<MongoBookModel> list = mt.find(query, MongoBookModel.class);

        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.name").is(authorName)); // Query on nested author collection
        return mt.find(query, Book.class);
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("lendings.startDate").gt(oneYearAgo)),
                Aggregation.lookup("lendings", "_id", "bookId", "lendings"), // Lookup to join lendings with books
                Aggregation.group("id")
                        .first("title").as("title")
                        .count().as("lendingCount"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")),
                Aggregation.limit(5)
        );

        AggregationResults<BookCountDTO> results = mt.aggregate(aggregation, "book", BookCountDTO.class);
        List<BookCountDTO> topBooks = results.getMappedResults();
        return new PageImpl<>(topBooks, pageable, topBooks.size());
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authors.authorNumber").is(authorNumber)); // Find books by author number
        return mt.find(query, Book.class);
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        Query mongoQuery = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            criteriaList.add(Criteria.where("title").regex("^" + title)); // Prefix match for title
        }

        if (genre != null && !genre.isEmpty()) {
            criteriaList.add(Criteria.where("genre").regex("^" + genre)); // Prefix match for genre
        }

        if (authorName != null && !authorName.isEmpty()) {
            criteriaList.add(Criteria.where("authors.name").regex("^" + authorName)); // Prefix match for author name
        }

        if (!criteriaList.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        //mongoQuery.with(page.toPageable()); // Apply pagination
        mongoQuery.with(Sort.by(Sort.Direction.ASC, "title")); // Sort by title alphabetically

        return mt.find(mongoQuery, Book.class);
    }

    @Override
    public List<Book> findTopXBooksFromGenre(int x, String genre) {
        // Match books by the specified genre
        MatchOperation matchGenre = Aggregation.match(Criteria.where("genre").is(genre));

        // Group by book ID and count the number of lendings
        AggregationOperation groupByBookAndCount = Aggregation.group("bookId")
                .count().as("lendingCount");

        // Sort by lending count in descending order
        SortOperation sortByLendingCountDesc = Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount"));

        // Limit the result to 'x' books
        AggregationOperation limit = Aggregation.limit(x);

        // Combine all operations into an aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                matchGenre,
                groupByBookAndCount,
                sortByLendingCountDesc,
                limit
        );

        // Execute the aggregation query
        AggregationResults<MongoBookModel> results = mt.aggregate(aggregation, "lendings", MongoBookModel.class);

        List<MongoBookModel> list = results.getMappedResults();

        return bookMapper.fromMongoBookModel(list);
    }

    @Override
    public Book save(Book book) {
        MongoBookModel mongoBook = bookMapper.toMongoBookModel(book);

        if (mongoBook.getPhoto() != null) {
            MongoPhotoModel photo = mongoBook.getPhoto();
            photo = mt.save(photo);
            mongoBook.setPhoto(photo);
        }

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
