package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;

public class AuthorJpaRepoImpl implements AuthorRepository {

    private final EntityManager em;

    public AuthorJpaRepoImpl(EntityManager em) {
        this.em = em;
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
