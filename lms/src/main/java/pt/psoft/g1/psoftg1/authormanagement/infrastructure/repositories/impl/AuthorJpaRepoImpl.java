package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorDTO;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AuthorJpaRepoImpl implements AuthorRepository {

    private final EntityManager em;

    public AuthorJpaRepoImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        return Optional.empty();
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
        String jpql = "SELECT new pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView(a.name.name, COUNT(l.id)) " +
                "FROM Author a " +
                "JOIN Lending l ON l.bookId IN (SELECT b.id FROM Book b WHERE :authorId MEMBER OF b.authorIds) " +
                "GROUP BY a.name " +
                "ORDER BY COUNT(l) DESC";

        // Create the query with the JPQL statement
        TypedQuery<AuthorLendingView> query = em.createQuery(jpql, AuthorLendingView.class);

        // Get the total count of results (for pagination)
        long totalResults = query.getResultList().size();

        // Apply pagination rules
        query.setFirstResult((int) pageableRules.getOffset());
        query.setMaxResults(pageableRules.getPageSize());

        // Get the paginated list of results
        List<AuthorLendingView> results = query.getResultList();

        return new PageImpl<>(results, pageableRules, totalResults);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        String jpql = "SELECT DISTINCT a " +
                "FROM Author a " +
                "WHERE a.id IN (SELECT coAuthorId FROM Book b JOIN b.authorIds coAuthorId " +
                "WHERE :authorNumber MEMBER OF b.authorIds AND coAuthorId <> :authorNumber)";

        // Create the query with the JPQL statement
        TypedQuery<Author> query = em.createQuery(jpql, Author.class);
        query.setParameter("authorNumber", authorNumber);

        // Execute and return the results
        return query.getResultList();
    }

    @Override
    public Author save(Author entity) {
        if (entity.getAuthorNumber() == 0) {
            em.persist(entity);
            return entity;
        } else {
            // check version
            JpaAuthorDTO author = em.find(JpaAuthorDTO.class, entity.getAuthorNumber());
            return em.merge(entity);
        }
    }

    @Override
    public void delete(Author entity) {
        em.remove(entity);
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("SELECT b FROM Author b", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(Long aLong) {
        return Optional.ofNullable(em.find(Author.class, aLong));
    }
}
