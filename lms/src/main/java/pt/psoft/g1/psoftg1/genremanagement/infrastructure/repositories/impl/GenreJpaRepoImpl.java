package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.domain.PageImpl;


@RequiredArgsConstructor
public class GenreJpaRepoImpl implements GenreRepository {

    private final EntityManager em;
    private final GenreMapper genreMapper;

    @Override
    public Optional<Genre> findByString(String genre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaGenreModel> query = cb.createQuery(JpaGenreModel.class);
        Root<JpaGenreModel> root = query.from(JpaGenreModel.class);

        query.select(root).where(cb.equal(root.get("genre"), genre));

        Optional<JpaGenreModel> jpaGenre = em.createQuery(query).getResultStream().findFirst();

        return jpaGenre.map(genreMapper::fromJpaGenre);
    }

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GenreBookCountDTO> query = cb.createQuery(GenreBookCountDTO.class);
        Root<Genre> genreRoot = query.from(Genre.class);
        Join<Genre, Book> bookJoin = genreRoot.join("books"); // Assuming the relationship exists

        // Create selection for GenreBookCountDTO
        query.select(cb.construct(GenreBookCountDTO.class, genreRoot.get("genre"), cb.count(bookJoin)))
                .groupBy(genreRoot).orderBy(cb.desc(cb.count(bookJoin)));

        TypedQuery<GenreBookCountDTO> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<GenreBookCountDTO> results = typedQuery.getResultList();

        long total = results.size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page) {
        int days = month.lengthOfMonth();
        LocalDate firstOfMonth = LocalDate.of(month.getYear(), month.getMonth(), 1);
        LocalDate lastOfMonth = LocalDate.of(month.getYear(), month.getMonth(), days);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GenreLendingsDTO> cq = cb.createQuery(GenreLendingsDTO.class);

        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book", JoinType.LEFT);
        Join<Book, Genre> genreJoin = bookJoin.join("genre", JoinType.LEFT);

        Expression<Long> loanCount = cb.count(lendingRoot.get("pk"));
        Expression<Number> dailyAvgLoans = cb.quot(cb.toDouble(loanCount), cb.literal(days));

        cq.multiselect(genreJoin, dailyAvgLoans);
        cq.groupBy(genreJoin.get("pk"));

        Predicate startDatePredicate = cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), firstOfMonth);
        Predicate endDatePredicate = cb.lessThanOrEqualTo(lendingRoot.get("startDate"), lastOfMonth);

        Predicate finalPredicate = cb.and(startDatePredicate, endDatePredicate);

        cq.where(finalPredicate);

        final TypedQuery<GenreLendingsDTO> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book");
        Join<Book, Genre> genreJoin = bookJoin.join("genre");

        Expression<Integer> yearExpr = cb.function("YEAR", Integer.class, lendingRoot.get("startDate"));
        Expression<Integer> monthExpr = cb.function("MONTH", Integer.class, lendingRoot.get("startDate"));
        Expression<Long> lendingDurationInDays = cb.diff(lendingRoot.get("returnedDate"), lendingRoot.get("startDate"));
        double nanoSecondsInADay = 86400.0 * 1E9;
        Expression<Number> durationInDays = cb.quot(cb.toDouble(lendingDurationInDays), nanoSecondsInADay);
        Expression<Double> averageDuration = cb.avg(cb.toDouble(durationInDays));

        cq.multiselect(genreJoin.get("genre"), yearExpr, monthExpr, averageDuration);
        cq.groupBy(genreJoin.get("genre"), yearExpr, monthExpr);
        cq.orderBy(cb.asc(yearExpr), cb.asc(monthExpr), cb.asc(genreJoin.get("genre")));

        Predicate startDatePredicate = cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), startDate);
        Predicate endDatePredicate = cb.lessThanOrEqualTo(lendingRoot.get("startDate"), endDate);
        Predicate returnedPredicate = cb.isNotNull(lendingRoot.get("returnedDate"));
        Predicate finalPredicate = cb.and(startDatePredicate, endDatePredicate, returnedPredicate);

        cq.where(finalPredicate);

        List<Tuple> results = em.createQuery(cq).getResultList();
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults = new HashMap<>();

        for (Tuple result : results) {
            String genre = result.get(0, String.class);
            int yearValue = result.get(1, Integer.class);
            int monthValue = result.get(2, Integer.class);
            Double averageDurationValue = result.get(3, Double.class);
            GenreLendingsDTO genreLendingsDTO = new GenreLendingsDTO(genre, averageDurationValue);

            groupedResults
                    .computeIfAbsent(yearValue, k -> new HashMap<>())
                    .computeIfAbsent(monthValue, k -> new ArrayList<>())
                    .add(genreLendingsDTO);
        }

        return getGenreLendingsPerMonthDtos(groupedResults);
    }


    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Lending> lendingRoot = cq.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book");
        Join<Book, Genre> genreJoin = bookJoin.join("genre");

        Expression<Integer> year = cb.function("YEAR", Integer.class, lendingRoot.get("startDate"));
        Expression<Integer> month = cb.function("MONTH", Integer.class, lendingRoot.get("startDate"));

        Expression<Long> lendingCount = cb.count(lendingRoot);

        cq.multiselect(genreJoin.get("genre"), year, month, lendingCount);
        cq.groupBy(genreJoin.get("genre"), year, month);

        // Predicate to filter the last 12 months
        LocalDate now = LocalDate.now();
        LocalDate twelveMonthsAgo = now.minusMonths(12);
        Predicate datePredicate = cb.between(lendingRoot.get("startDate"),
                java.sql.Date.valueOf(twelveMonthsAgo),
                java.sql.Date.valueOf(now));

        cq.where(datePredicate);
        cq.orderBy(cb.asc(year), cb.asc(month), cb.asc(genreJoin.get("genre")));

        TypedQuery<Tuple> query = em.createQuery(cq);
        List<Tuple> results = query.getResultList();

        // Grouping results by year and month
        Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults = new HashMap<>();

        for (Tuple result : results) {
            String genre = result.get(0, String.class);
            int resultYear = result.get(1, Integer.class);
            int resultMonth = result.get(2, Integer.class);
            long count = result.get(3, Long.class);
            GenreLendingsDTO genreLendingsDTO = new GenreLendingsDTO(genre, count);

            groupedResults
                    .computeIfAbsent(resultYear, k -> new HashMap<>())
                    .computeIfAbsent(resultMonth, k -> new ArrayList<>())
                    .add(genreLendingsDTO);
        }

        return getGenreLendingsPerMonthDtos(groupedResults);
    }

    @Override
    public List<Genre> findAllGenres() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Genre> query = cb.createQuery(Genre.class);
        query.from(Genre.class);  // Create a query for the Genre class

        return em.createQuery(query).getResultList();  // Execute and return the result list
    }

    @Override
    public Optional<Genre> findReaderMostRequestedGenre(String readerNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<JpaLendingModel> lendingRoot = cq.from(JpaLendingModel.class);
        Join<JpaLendingModel, JpaBookModel> bookJoin = lendingRoot.join("book");
        Join<JpaBookModel, JpaGenreModel> genreJoin = bookJoin.join("genre");

        Expression<Long> lendingCount = cb.count(lendingRoot);
        cq.multiselect(genreJoin, lendingCount);
        cq.groupBy(genreJoin);
        cq.orderBy(cb.desc(lendingCount));

        Predicate readerPredicate = cb.equal(lendingRoot.get("readerDetails").get("readerNumber"), readerNumber);
        cq.where(readerPredicate);

        TypedQuery<Tuple> query = em.createQuery(cq);
        query.setMaxResults(1);

        List<Tuple> results = query.getResultList();
        if (results.isEmpty()) {
            return Optional.empty();
        }

        Optional<JpaGenreModel> m = Optional.ofNullable(results.get(0).get(0, JpaGenreModel.class));

        return m.map(genreMapper::fromJpaGenre);
    }

    @Override
    public List<Genre> getTopYGenresMostLent(int y) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<JpaLendingModel> lendingRoot = cq.from(JpaLendingModel.class);
        Join<JpaLendingModel, JpaBookModel> bookJoin = lendingRoot.join("book");
        Join<JpaBookModel, JpaGenreModel> genreJoin = bookJoin.join("genre");

        Expression<Long> lendingCount = cb.count(lendingRoot);
        cq.multiselect(genreJoin, lendingCount);
        cq.groupBy(genreJoin);
        cq.orderBy(cb.desc(lendingCount));

        TypedQuery<Tuple> query = em.createQuery(cq);
        query.setMaxResults(y);

        List<Tuple> results = query.getResultList();
        List<Genre> genres = new ArrayList<>();
        for (Tuple result : results) {
            genres.add(genreMapper.fromJpaGenre(result.get(0, JpaGenreModel.class)));
        }

        return genres;
    }

    @Override
    public Genre save(Genre genre) {
        JpaGenreModel jpaGenre = genreMapper.toJpaGenre(genre);
        if (genre.getPk() == null) {
            em.persist(jpaGenre);
        } else {
            em.merge(jpaGenre);
        }
        return genreMapper.fromJpaGenre(jpaGenre);
    }

    @Override
    public void delete(Genre genre) {
        JpaGenreModel jpaGenre = genreMapper.toJpaGenre(genre);

        if (em.contains(jpaGenre)) {
            em.remove(jpaGenre);  // If managed, remove directly
        } else {
            JpaGenreModel managedGenre = em.merge(jpaGenre);  // If detached, merge to manage
            em.remove(managedGenre);  // Then remove
        }
    }

    @Override
    public List<Genre> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaGenreModel> query = cb.createQuery(JpaGenreModel.class);
        query.from(JpaGenreModel.class);

        List<JpaGenreModel> jpaGenres = em.createQuery(query).getResultList();
        List<Genre> genres = new ArrayList<>();
        for (JpaGenreModel i : jpaGenres) {
            genres.add(genreMapper.fromJpaGenre(i));
        }
        return genres;
    }

    @Override
    public Optional<Genre> findById(String genreId) {
        Optional<JpaGenreModel> jpaGenre = Optional.ofNullable(em.find(JpaGenreModel.class, genreId));  // Use find method to get Author by ID
        return jpaGenre.map(genreMapper::fromJpaGenre);
    }

    @NotNull
    private List<GenreLendingsPerMonthDTO> getGenreLendingsPerMonthDtos(Map<Integer, Map<Integer,
            List<GenreLendingsDTO>>> groupedResults) {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, List<GenreLendingsDTO>>> yearEntry : groupedResults.entrySet()) {
            int yearValue = yearEntry.getKey();
            for (Map.Entry<Integer, List<GenreLendingsDTO>> monthEntry : yearEntry.getValue().entrySet()) {
                int monthValue = monthEntry.getKey();
                List<GenreLendingsDTO> values = monthEntry.getValue();
                lendingsPerMonth.add(new GenreLendingsPerMonthDTO(yearValue, monthValue, values));
            }
        }

        return lendingsPerMonth;
    }
}
