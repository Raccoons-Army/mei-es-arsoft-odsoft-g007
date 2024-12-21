package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.mapper.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class AuthorJpaRepoImpl implements AuthorRepository {

    private final EntityManager em;
    private final AuthorMapper authorMapper;

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaAuthorModel> query = cb.createQuery(JpaAuthorModel.class);
        Root<JpaBookModel> bookRoot = query.from(JpaBookModel.class);
        Join<JpaBookModel, JpaAuthorModel> coAuthorJoin = bookRoot.join("authors");

        Subquery<JpaBookModel> subquery = query.subquery(JpaBookModel.class);
        Root<JpaBookModel> subqueryRoot = subquery.from(JpaBookModel.class);
        Join<JpaBookModel, JpaAuthorModel> authorJoin = subqueryRoot.join("authors");

        // Subquery to get books for the specific author
        subquery.select(subqueryRoot)
                .where(cb.equal(authorJoin.get("authorNumber"), authorNumber));

        query.select(coAuthorJoin)
                .where(cb.and(
                        cb.in(bookRoot).value(subquery),  // Books from the main query that are in the subquery
                        cb.notEqual(coAuthorJoin.get("authorNumber"), authorNumber) // Co-authors must not have the same authorNumber
                ));

        List<JpaAuthorModel> m = em.createQuery(query).getResultList();

        return authorMapper.fromJpaAuthor(m);
    }

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaAuthorModel> query = cb.createQuery(JpaAuthorModel.class);
        Root<JpaAuthorModel> root = query.from(JpaAuthorModel.class);

        query.select(root)
                .where(cb.equal(root.get("authorNumber"), authorNumber));

        Optional<JpaAuthorModel> jpaAuthor = em.createQuery(query).getResultStream().findFirst();

        return jpaAuthor.map(authorMapper::fromJpaAuthor);
    }

    @Override
    public Author save(Author author) {
        JpaAuthorModel jpaAuthor = authorMapper.toJpaAuthor(author);

        if (author.getPk() != null) {
            // Update existing entity
            em.merge(jpaAuthor);
        } else {
            // Save new entity
            em.persist(jpaAuthor);
        }
       return authorMapper.fromJpaAuthor(jpaAuthor);
    }

    @Override
    public void delete(Author author) {
        JpaAuthorModel jpaAuthor = authorMapper.toJpaAuthor(author);

        if (em.contains(jpaAuthor)) {
            em.remove(jpaAuthor);  // If managed, remove directly
        } else {
            JpaAuthorModel managedAuthor = em.merge(jpaAuthor);  // If detached, merge to manage
            em.remove(managedAuthor);  // Then remove
        }
    }

    @Override
    public List<Author> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaAuthorModel> query = cb.createQuery(JpaAuthorModel.class);
        query.from(JpaAuthorModel.class);  // Create query for Author class

        List<JpaAuthorModel> jpaAuthors = em.createQuery(query).getResultList();
        return authorMapper.fromJpaAuthor(jpaAuthors);
    }

    @Override
    public Optional<Author> findById(String authorId) {
        Optional<JpaAuthorModel> jpaAuthor = Optional.ofNullable(em.find(JpaAuthorModel.class, authorId));  // Use find method to get Author by ID
        return jpaAuthor.map(authorMapper::fromJpaAuthor);
    }
}
