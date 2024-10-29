package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<Book> books = new ArrayList<>();
        for (JpaBookModel i : list) {
            books.add(bookMapper.fromJpaBookModel(i));
        }
        return books;
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
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
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
    public List<Book> findTopXBooksFromGenre(int x, String genre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaBookModel> query = cb.createQuery(JpaBookModel.class);
        Root<JpaLendingModel> lending = query.from(JpaLendingModel.class);
        Join<JpaLendingModel, JpaBookModel> bookJoin = lending.join("book");
        Join<JpaBookModel, JpaGenreModel> genreJoin = bookJoin.join("genre");
        query.groupBy(bookJoin.get("pk"));
        query.where(cb.equal(genreJoin.get("genre"), genre));

        // Order by the number of lendings
        Expression<Long> lendingCount = cb.count(lending.get("pk"));
        query.orderBy(cb.desc(lendingCount));

        // Select the book
        query.select(bookJoin);

        List<JpaBookModel> list = em.createQuery(query)
                .setMaxResults(x)
                .getResultList();

        List<Book> books = new ArrayList<>();
        for (JpaBookModel i : list) {
            books.add(bookMapper.fromJpaBookModel(i));
        }
        return books;
    }

    @Override
    public Book save(Book book) {
        JpaBookModel jpaBook = bookMapper.toJpaBookModel(book);
        if (book.getPk() == null) {
            em.persist(jpaBook);
        } else {
            em.merge(jpaBook);
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
        List<Book> books = new ArrayList<>();
        for (JpaBookModel i : jpaBooks) {
            books.add(bookMapper.fromJpaBookModel(i));
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long bookId) {
        Optional<JpaBookModel> jpaBook = Optional.ofNullable(em.find(JpaBookModel.class, bookId));
        return jpaBook.map(bookMapper::fromJpaBookModel);
    }
}
