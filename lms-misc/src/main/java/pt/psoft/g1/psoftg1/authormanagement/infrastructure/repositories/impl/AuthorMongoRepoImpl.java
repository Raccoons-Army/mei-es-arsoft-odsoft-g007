package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.mapper.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthorMongoRepoImpl implements AuthorRepository {

    public final MongoTemplate mt;

    private final AuthorMapper authorMapper;

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
