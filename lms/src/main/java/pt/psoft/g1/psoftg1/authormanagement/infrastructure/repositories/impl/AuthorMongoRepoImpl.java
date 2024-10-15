package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;

public class AuthorMongoRepoImpl implements AuthorRepository {

    public MongoTemplate mongoTemplate;

    public AuthorMongoRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return null;
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return null;
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageableRules) {
        return null;
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        return null;
    }

    @Override
    public Author save(Author entity) {
        return null;
    }

    @Override
    public void delete(Author entity) {

    }

    @Override
    public List<Author> findAll() {
        return null;
    }

    @Override
    public Author findById(Long aLong) {
        return null;
    }
}
