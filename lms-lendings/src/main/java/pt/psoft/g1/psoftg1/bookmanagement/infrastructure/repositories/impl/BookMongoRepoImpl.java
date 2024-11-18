package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
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
