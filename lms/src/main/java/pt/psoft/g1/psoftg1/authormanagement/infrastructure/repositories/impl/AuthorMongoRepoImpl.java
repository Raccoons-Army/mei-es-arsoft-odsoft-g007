package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthorMongoRepoImpl implements AuthorRepository {

    public final MongoTemplate mt;
    
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
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return null;
    }

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        return Optional.empty();
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
