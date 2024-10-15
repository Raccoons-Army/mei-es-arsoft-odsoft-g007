package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthorJpaRepoImpl implements AuthorRepository {

    private final EntityManager em;
    
    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);

        query.select(root)
                .where(cb.like(root.get("name"), name + "%"));  // Uses SQL LIKE for prefix matching

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Author> searchByNameName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);

        query.select(root)
                .where(cb.equal(root.get("name"), name));  // Matches the exact name

        return em.createQuery(query).getResultList();
    }

    @Override
    public Page<AuthorLendingView> findTopAuthorByLendings(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AuthorLendingView> query = cb.createQuery(AuthorLendingView.class);
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Author> authorJoin = bookRoot.join("authors");
        Join<Book, Lending> lendingJoin = bookRoot.join("lendings");

        // Create selection for AuthorLendingView
        query.select(cb.construct(AuthorLendingView.class, authorJoin.get("name").get("name"),
                        cb.count(lendingJoin.get("pk"))))
                .groupBy(authorJoin.get("name"))
                .orderBy(cb.desc(cb.count(lendingJoin)));

        TypedQuery<AuthorLendingView> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<AuthorLendingView> results = typedQuery.getResultList();
        long total = results.size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(Long authorNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Author> coAuthorJoin = bookRoot.join("authors");

        Subquery<Book> subquery = query.subquery(Book.class);
        Root<Book> subqueryRoot = subquery.from(Book.class);
        Join<Book, Author> authorJoin = subqueryRoot.join("authors");

        // Subquery to get books for the specific author
        subquery.select(subqueryRoot)
                .where(cb.equal(authorJoin.get("authorNumber"), authorNumber));

        query.select(coAuthorJoin)
                .where(cb.and(
                        cb.in(bookRoot).value(subquery),  // Books from the main query that are in the subquery
                        cb.notEqual(coAuthorJoin.get("authorNumber"), authorNumber) // Co-authors must not have the same authorNumber
                ));

        return em.createQuery(query).getResultList();
    }

    @Override
    public Optional<Author> findByAuthorNumber(Long authorNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);

        query.select(root)
                .where(cb.equal(root.get("authorNumber"), authorNumber));

        return em.createQuery(query)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Author save(Author entity) {
        if (entity.getId() != null) {
            // Update existing entity
            return em.merge(entity);
        } else {
            // Save new entity
            em.persist(entity);
            return entity;
        }
    }

    @Override
    public void delete(Author entity) {
        if (em.contains(entity)) {
            em.remove(entity);  // If managed, remove directly
        } else {
            Author managedAuthor = em.merge(entity);  // If detached, merge to manage
            em.remove(managedAuthor);  // Then remove
        }
    }

    @Override
    public List<Author> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        query.from(Author.class);  // Create query for Author class

        return em.createQuery(query).getResultList();  // Execute and return result
    }

    @Override
    public Author findById(Long aLong) {
        return em.find(Author.class, aLong);  // Use find method to get Author by ID
    }
}
