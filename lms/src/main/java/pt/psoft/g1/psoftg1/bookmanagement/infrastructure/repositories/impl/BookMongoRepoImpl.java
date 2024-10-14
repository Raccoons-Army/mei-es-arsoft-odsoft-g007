package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookMongoRepoImpl implements BookRepository {

    public MongoTemplate mongoTemplate;

    public BookMongoRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        Query query = new Query(Criteria.where("genre.genre").is(genre));
        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = new Query(Criteria.where("title.title").is(title));
        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return null;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.empty();
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        Query query = new Query(Criteria.where("lending.startDate").gt(oneYearAgo));
        query.with(pageable);

        List<BookCountDTO> topBooks = mongoTemplate.find(query, BookCountDTO.class);

        return new PageImpl<>(topBooks, pageable, topBooks.size());
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        Query query = new Query(Criteria.where("authors").is(authorNumber));
        return mongoTemplate.find(query, Book.class);
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        Query mongoQuery = new Query();

        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("title.title").regex("^" + query.getTitle(), "i"));
        }

        if (query.getGenre() != null && !query.getGenre().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("genre.genre").regex("^" + query.getGenre(), "i"));
        }

        if (query.getAuthorName() != null && !query.getAuthorName().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("authors.name").regex("^" + query.getAuthorName(), "i"));
        }

        //mongoQuery.with(page.toPageRequest());

        return mongoTemplate.find(mongoQuery, Book.class);
    }

    @Override
    public Book save(Book entity) {
        return mongoTemplate.save(entity);
    }

    @Override
    public void delete(Book book) {
        mongoTemplate.remove(book);
    }

    @Override
    public List<Book> findAll() {
        return mongoTemplate.findAll(Book.class);
    }

    @Override
    public Optional<Book> findById(Isbn isbn) {
        Book book = mongoTemplate.findById(isbn, Book.class);
        return Optional.ofNullable(book);
    }
}
