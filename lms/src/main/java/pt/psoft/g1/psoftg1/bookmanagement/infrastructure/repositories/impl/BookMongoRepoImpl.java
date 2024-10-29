package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

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
        query.addCriteria(Criteria.where("genre.genre").is(genre)); // Accessing nested property
        return mt.find(query, Book.class);
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title.title").is(title)); // Accessing nested property
        return mt.find(query, Book.class);
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
            criteriaList.add(Criteria.where("title.title").regex("^" + title)); // Prefix match for title
        }

        if (genre != null && !genre.isEmpty()) {
            criteriaList.add(Criteria.where("genre.genre").regex("^" + genre)); // Prefix match for genre
        }

        if (authorName != null && !authorName.isEmpty()) {
            criteriaList.add(Criteria.where("authors.name").regex("^" + authorName)); // Prefix match for author name
        }

        if (!criteriaList.isEmpty()) {
            mongoQuery.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        //mongoQuery.with(page.toPageable()); // Apply pagination
        mongoQuery.with(Sort.by(Sort.Direction.ASC, "title.title")); // Sort by title alphabetically

        return mt.find(mongoQuery, Book.class);
    }

    @Override
    public List<Book> findTopXBooksFromGenre(int x, String genre) {
        return null;
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
