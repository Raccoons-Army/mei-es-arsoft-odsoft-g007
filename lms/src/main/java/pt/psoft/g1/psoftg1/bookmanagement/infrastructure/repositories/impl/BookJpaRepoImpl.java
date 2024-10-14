package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookJpaRepoImpl implements BookRepository {

    private final EntityManager em;
    @Autowired
    private final BookMapper mapper;

    public BookJpaRepoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Book> findByGenre(String genre) {
        String query = "SELECT b FROM Book b WHERE b.genre = :genre";
        return em.createQuery(query, Book.class)
                .setParameter("genre", genre)
                .getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        String query = "SELECT b FROM Book b WHERE b.title.title = :title";
        return em.createQuery(query, Book.class)
                .setParameter("title", title)
                .getResultList();
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        String query = "SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName";
        return em.createQuery(query, Book.class)
                .setParameter("authorName", authorName)
                .getResultList();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        try {
            String query = "SELECT b FROM Book b WHERE b.isbn.isbn = :isbn";
//            JpaBookDTO book = em.createQuery(query, Book.class)
//                    .setParameter("isbn", isbn)
//                    .getSingleResult();
            BookMapper.fromJpaToDomain(null, null);
            return Optional.of(book);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        String query = "SELECT new pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO(b, COUNT(l)) " +
                "FROM Book b JOIN Lending l ON l.book = b " +
                "WHERE l.startDate > :oneYearAgo GROUP BY b ORDER BY COUNT(l) DESC";
        List<BookCountDTO> topBooks = em.createQuery(query, BookCountDTO.class)
                .setParameter("oneYearAgo", oneYearAgo)
                .setMaxResults(5)
                .getResultList();
        return new PageImpl<>(topBooks, pageable, topBooks.size());
    }

    @Override
    public List<Book> findBooksByAuthorNumber(Long authorNumber) {
        String query = "SELECT b FROM Book b JOIN b.authors a WHERE a.authorNumber = :authorNumber";
        return em.createQuery(query, Book.class)
                .setParameter("authorNumber", authorNumber)
                .getResultList();
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        String title = query.getTitle();
        String genre = query.getGenre();
        String authorName = query.getAuthorName();

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        final Root<Book> root = cq.from(Book.class);
        final Join<Book, Genre> genreJoin = root.join("genre");
        final Join<Book, Author> authorJoin = root.join("authors");
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(title))
            where.add(cb.like(root.get("title").get("title"), title + "%"));

        if (StringUtils.hasText(genre))
            where.add(cb.like(genreJoin.get("genre"), genre + "%"));

        if (StringUtils.hasText(authorName))
            where.add(cb.like(authorJoin.get("name").get("name"), authorName + "%"));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("title"))); // Order by title, alphabetically

        final TypedQuery<Book> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public Book save(Book entity) {
        if (entity.getId() == null || entity.getId().toString() == "") {
            em.persist(entity);
            return entity;
        } else {
            // check version
            JpaBookDTO book = em.find(JpaBookDTO.class, entity.getId());
            return em.merge(entity);
        }
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    public Optional<Book> findById(Isbn isbn) {
       return Optional.ofNullable(em.find(Book.class, isbn));
    }
}
