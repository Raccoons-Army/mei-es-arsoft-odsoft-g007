package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

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
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.mapper.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthorMongoRepoImpl implements AuthorRepository {

    public final MongoTemplate mt;

    private final AuthorMapper authorMapper;

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex("^" + name));
        List<MongoAuthorModel> mongoAuthors =  mt.find(query, MongoAuthorModel.class);
        return authorMapper.fromMongoAuthor(mongoAuthors);
    }

    @Override
    public List<Author> searchByNameName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));

        List<MongoAuthorModel> mongoAuthors = mt.find(query, MongoAuthorModel.class);

        return authorMapper.fromMongoAuthor(mongoAuthors);
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("book", "authors", "_id", "books"),
                Aggregation.unwind("books"),
                Aggregation.lookup("lending", "books._id", "bookId", "lendings"),
                Aggregation.unwind("lendings"),
                Aggregation.group("authors.name")
                        .count().as("lendingCount")
                        .first("authors.name").as("authorName"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "lendingCount")),
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );

        AggregationResults<AuthorLendingView> results = mt.aggregate(aggregation, "author", AuthorLendingView.class);
        List<AuthorLendingView> authorLendingViews = results.getMappedResults();
        long total = authorLendingViews.size(); // Consider calculating total count separately if needed

        return new PageImpl<>(authorLendingViews, pageable, total);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        Query subquery = new Query();
        subquery.addCriteria(Criteria.where("authors.authorNumber").is(authorNumber));
        List<MongoBookModel> booksByAuthor = mt.find(subquery, MongoBookModel.class);

        Query query = new Query();
        query.addCriteria(Criteria.where("books").in(booksByAuthor)
                .and("authors.authorNumber").ne(authorNumber));

        List<MongoAuthorModel> list = mt.find(query, MongoAuthorModel.class);
        return authorMapper.fromMongoAuthor(list);
    }

    @Override
    public Author save(Author author) {
        MongoAuthorModel mongoAuthor = authorMapper.toMongoAuthor(author);

        if (mongoAuthor.getPhoto() != null) {
            MongoPhotoModel photo = mongoAuthor.getPhoto();
            photo = mt.save(photo);
            mongoAuthor.setPhoto(photo);
        }

        MongoAuthorModel savedAuthor = mt.save(mongoAuthor);
        return authorMapper.fromMongoAuthor(savedAuthor);
    }

    @Override
    public void delete(Author author) {
        MongoAuthorModel mongoAuthor = authorMapper.toMongoAuthor(author);

        mt.remove(mongoAuthor);
    }

    @Override
    public List<Author> findAll() {
        List<MongoAuthorModel> mongoAuthors = mt.findAll(MongoAuthorModel.class);

        return authorMapper.fromMongoAuthor(mongoAuthors);
    }

    @Override
    public Optional<Author> findById(String id) {
        MongoAuthorModel mongoAuthor = mt.findById(id, MongoAuthorModel.class);

        return Optional.ofNullable(mongoAuthor)
                .map(authorMapper::fromMongoAuthor);
    }

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("authorNumber").is(authorNumber));
        MongoAuthorModel mongoAuthor = mt.findOne(query, MongoAuthorModel.class);

        return Optional.ofNullable(mongoAuthor)
                .map(authorMapper::fromMongoAuthor);
    }

}
