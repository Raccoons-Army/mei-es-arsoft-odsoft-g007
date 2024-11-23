package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class BookJpaRepoImpl implements BookRepository {

    private final EntityManager em;
    private final BookMapper bookMapper;

    @Override
    public List<Book> findByGenre(String genre) {
        String query = "SELECT b FROM JpaBookModel b WHERE b.genre.genre = :genre";
        List<JpaBookModel> list = em.createQuery(query, JpaBookModel.class)
                .setParameter("genre", genre)
                .getResultList();

        return bookMapper.fromJpaBookModel(list);
    }

    @Override
    public List<Book> findByTitle(String title) {
        String query = "SELECT b FROM JpaBookModel b WHERE b.title = :title";
        List<JpaBookModel> list = em.createQuery(query, JpaBookModel.class)
                .setParameter("title", title)
                .getResultList();

        return bookMapper.fromJpaBookModel(list);
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        String query = "SELECT b FROM JpaBookModel b JOIN b.authors a WHERE a.name = :authorName";
        List<JpaBookModel> list = em.createQuery(query, JpaBookModel.class)
                .setParameter("authorName", authorName)
                .getResultList();

        return bookMapper.fromJpaBookModel(list);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        try {
            String query = "SELECT b FROM JpaBookModel b WHERE b.isbn = :isbn";
            JpaBookModel book = em.createQuery(query, JpaBookModel.class)
                    .setParameter("isbn", isbn)
                    .getSingleResult();
            return Optional.of(bookMapper.fromJpaBookModel(book));
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        String query = "SELECT b FROM JpaBookModel b JOIN b.authors a WHERE a.authorNumber = :authorNumber";
        List<JpaBookModel> m = em.createQuery(query, JpaBookModel.class)
                .setParameter("authorNumber", authorNumber)
                .getResultList();

        return bookMapper.fromJpaBookModel(m);
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<JpaBookModel> cq = cb.createQuery(JpaBookModel.class);
        final Root<JpaBookModel> root = cq.from(JpaBookModel.class);
        final Join<JpaBookModel, JpaGenreModel> genreJoin = root.join("genre");
        final Join<JpaBookModel, JpaAuthorModel> authorJoin = root.join("authors");
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(title))
            where.add(cb.like(root.get("title"), title + "%"));

        if (StringUtils.hasText(genre))
            where.add(cb.like(genreJoin.get("genre"), genre + "%"));

        if (StringUtils.hasText(authorName))
            where.add(cb.like(authorJoin.get("name"), authorName + "%"));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("title"))); // Order by title, alphabetically

        final TypedQuery<JpaBookModel> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return bookMapper.fromJpaBookModel(q.getResultList());
    }

    @Override
    public Book save(Book book) {
        JpaBookModel jpaBook = bookMapper.toJpaBookModel(book);

        try {
            if (book.getPk() == null) {
                em.persist(jpaBook);
            } else {
                em.merge(jpaBook);
            }
        } catch (PersistenceException e) {
            // Log detailed information about the persistence error
            System.err.println("Error persisting JpaBookModel: " + e.getMessage());
        }

        return bookMapper.fromJpaBookModel(jpaBook);
    }


    @Override
    public void delete(Book book) {
        JpaBookModel jpaBook = bookMapper.toJpaBookModel(book);

        if (em.contains(jpaBook)) {
            em.remove(jpaBook);  // If managed, remove directly
        } else {
            JpaBookModel managedBook = em.merge(jpaBook);  // If detached, merge to manage
            em.remove(managedBook);  // Then remove
        }
    }

    @Override
    public List<Book> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaBookModel> query = cb.createQuery(JpaBookModel.class);
        query.from(JpaBookModel.class);

        List<JpaBookModel> jpaBooks = em.createQuery(query).getResultList();

        return bookMapper.fromJpaBookModel(jpaBooks);
    }

    @Override
    public Optional<Book> findById(String bookId) {
        Optional<JpaBookModel> jpaBook = Optional.ofNullable(em.find(JpaBookModel.class, bookId));
        return jpaBook.map(bookMapper::fromJpaBookModel);
    }
}
