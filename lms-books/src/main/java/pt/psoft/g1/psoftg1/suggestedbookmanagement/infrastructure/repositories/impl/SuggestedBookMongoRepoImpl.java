package pt.psoft.g1.psoftg1.suggestedbookmanagement.infrastructure.repositories.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.dbSchema.MongoSuggestedBookModel;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.mapper.SuggestedBookMapper;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.repositories.SuggestedBookRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class SuggestedBookMongoRepoImpl implements SuggestedBookRepository {

    private final MongoTemplate mt;
    private final SuggestedBookMapper suggestedBookMapper;

    @Override
    public SuggestedBook save(SuggestedBook suggestedBook) {
        MongoSuggestedBookModel mongoSuggestedBook = suggestedBookMapper.toMongoSuggestedBookModel(suggestedBook);
        MongoSuggestedBookModel savedSuggestedBook = mt.save(mongoSuggestedBook);
        return suggestedBookMapper.fromMongoSuggestedBookModel(savedSuggestedBook);
    }

    @Override
    public void delete(SuggestedBook suggestedBook) {
        MongoSuggestedBookModel mongoSuggestedBook = suggestedBookMapper.toMongoSuggestedBookModel(suggestedBook);
        mt.remove(mongoSuggestedBook);
    }

    @Override
    public List<SuggestedBook> findAll() {
        List<MongoSuggestedBookModel> mongoSuggestedBooks = mt.findAll(MongoSuggestedBookModel.class);
        return suggestedBookMapper.fromMongoSuggestedBookModel(mongoSuggestedBooks);
    }

    @Override
    public Optional<SuggestedBook> findById(String suggestedBookId) {
        MongoSuggestedBookModel mongoSuggestedBook = mt.findById(suggestedBookId, MongoSuggestedBookModel.class);

        return Optional.ofNullable(mongoSuggestedBook)
                .map(suggestedBookMapper::fromMongoSuggestedBookModel);
    }

    @Override
    public Optional<SuggestedBook> findByIsbn(String isbn) {
        Query query = new Query();
        query.addCriteria(Criteria.where("isbn").is(isbn));
        MongoSuggestedBookModel mongoSuggestedBook = mt.findOne(query, MongoSuggestedBookModel.class);

        return Optional.ofNullable(mongoSuggestedBook)
                .map(suggestedBookMapper::fromMongoSuggestedBookModel);
    }
}
