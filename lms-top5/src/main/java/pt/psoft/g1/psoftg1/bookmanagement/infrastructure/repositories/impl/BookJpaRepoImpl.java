package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Page<BookCountDTO> findTopXBooksLent(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<JpaLendingModel> lendingRoot = query.from(JpaLendingModel.class);
        Join<JpaLendingModel, JpaBookModel> bookJoin = lendingRoot.join("book");

        query.select(cb.array(bookJoin, cb.count(lendingRoot)))
                .groupBy(bookJoin)
                .orderBy(cb.desc(cb.count(lendingRoot)));

        TypedQuery<Object[]> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Object[]> results = typedQuery.getResultList();

        // Map results to DTO
        List<BookCountDTO> topBooks = results.stream()
                .map(result -> new BookCountDTO(bookMapper.fromJpaBookModel((JpaBookModel) result[0]), (Long) result[1]))
                .collect(Collectors.toList());

        return new PageImpl<>(topBooks, pageable, topBooks.size());

    }

    @Override
    public Book save(Book book) {
        JpaBookModel jpaBook = bookMapper.toJpaBookModel(book);

        try {
            if (book.getVersion() == null) {
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
