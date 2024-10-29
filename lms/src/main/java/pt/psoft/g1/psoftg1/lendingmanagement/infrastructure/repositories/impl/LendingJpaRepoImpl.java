package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LendingJpaRepoImpl implements LendingRepository {

    private final EntityManager em;
    private final LendingMapper lendingMapper;

    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaLendingModel> cq = cb.createQuery(JpaLendingModel.class);
        Root<JpaLendingModel> lending = cq.from(JpaLendingModel.class);

        // Define the criteria: lendingNumber should match
        cq.select(lending).where(cb.equal(lending.get("lendingNumber"), lendingNumber));

        // Execute the query
        Optional<JpaLendingModel> m = em.createQuery(cq).getResultStream().findFirst();

        return m.map(lendingMapper::fromJpaLendingModel);
    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lending> query = cb.createQuery(Lending.class);
        Root<Lending> lendingRoot = query.from(Lending.class);
        Join<Lending, Book> bookJoin = lendingRoot.join("book"); // Assuming the relationship to Book
        Join<Lending, ReaderDetails> readerJoin = lendingRoot.join("readerDetails"); // Assuming the relationship to ReaderDetails

        // Build the where clause
        query.select(lendingRoot)
                .where(cb.and(
                        cb.equal(bookJoin.get("isbn").get("isbn"), isbn), // Adjust path as necessary
                        cb.equal(readerJoin.get("readerNumber").get("readerNumber"), readerNumber) // Adjust path as necessary
                ));

        return em.createQuery(query).getResultList();
    }

    @Override
    public int getCountFromCurrentYear() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Lending> lendingRoot = query.from(Lending.class);

        // Count lendings where the start date is in the current year
        query.select(cb.count(lendingRoot))
                .where(cb.equal(cb.function("YEAR", Integer.class, lendingRoot.get("startDate")), cb.function("YEAR", Integer.class, cb.currentDate())));

        return em.createQuery(query).getSingleResult().intValue();
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lending> query = cb.createQuery(Lending.class);
        Root<Lending> lendingRoot = query.from(Lending.class);
        Join<Lending, ReaderDetails> readerJoin = lendingRoot.join("readerDetails"); // Assuming the relationship to ReaderDetails

        // Build the where clause
        query.select(lendingRoot)
                .where(cb.and(
                        cb.equal(readerJoin.get("readerNumber").get("readerNumber"), readerNumber), // Adjust path as necessary
                        cb.isNull(lendingRoot.get("returnedDate"))
                ));

        return em.createQuery(query).getResultList();
    }

    @Override
    public Double getAverageDuration() {
        // Using native query to calculate average duration
        return (Double) em.createNativeQuery(
                        "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) FROM Lending l")
                .getSingleResult();
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        // Using native query to calculate average duration by ISBN
        return (Double) em.createNativeQuery(
                        "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) " +
                                "FROM Lending l " +
                                "JOIN Book b ON l.BOOK_PK = b.PK " +
                                "WHERE b.ISBN = :isbn")
                .setParameter("isbn", isbn)
                .getSingleResult();
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        final Root<Lending> root = cq.from(Lending.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        // Select overdue lendings where returnedDate is null and limitDate is before the current date
        where.add(cb.isNull(root.get("returnedDate")));
        where.add(cb.lessThan(root.get("limitDate"), LocalDate.now()));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("limitDate"))); // Order by limitDate, oldest first

        final TypedQuery<Lending> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate){
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Lending> cq = cb.createQuery(Lending.class);
        final Root<Lending> lendingRoot = cq.from(Lending.class);
        final Join<Lending, Book> bookJoin = lendingRoot.join("book");
        final Join<Lending, ReaderDetails> readerDetailsJoin = lendingRoot.join("readerDetails");
        cq.select(lendingRoot);

        final List<Predicate> where = new ArrayList<>();

        if (StringUtils.hasText(readerNumber))
            where.add(cb.like(readerDetailsJoin.get("readerNumber").get("readerNumber"), readerNumber));
        if (StringUtils.hasText(isbn))
            where.add(cb.like(bookJoin.get("isbn").get("isbn"), isbn));
        if (returned != null){
            if(returned){
                where.add(cb.isNotNull(lendingRoot.get("returnedDate")));
            }else{
                where.add(cb.isNull(lendingRoot.get("returnedDate")));
            }
        }
        if(startDate!=null)
            where.add(cb.greaterThanOrEqualTo(lendingRoot.get("startDate"), startDate));
        if(endDate!=null)
            where.add(cb.lessThanOrEqualTo(lendingRoot.get("startDate"), endDate));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(lendingRoot.get("lendingNumber")));

        final TypedQuery<Lending> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }


    @Override
    public Lending save(Lending lending) {
        JpaLendingModel jpaLending = lendingMapper.toJpaLendingModel(lending);
        if (lending.getPk() == null) {
            em.persist(jpaLending);
        } else {
            em.merge(jpaLending);
        }
        return lendingMapper.fromJpaLendingModel(jpaLending);
    }

    @Override
    public void delete(Lending lending) {
        JpaLendingModel jpaLending = lendingMapper.toJpaLendingModel(lending);

        if (em.contains(jpaLending)) {
            em.remove(jpaLending);  // If managed, remove directly
        } else {
            JpaLendingModel managedLending = em.merge(jpaLending);  // If detached, merge to manage
            em.remove(managedLending);  // Then remove
        }
    }

    @Override
    public List<Lending> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaLendingModel> query = cb.createQuery(JpaLendingModel.class);
        query.from(JpaLendingModel.class);

        List<JpaLendingModel> jpaLendings = em.createQuery(query).getResultList();
        return lendingMapper.fromJpaLendingModel(jpaLendings);
    }

    @Override
    public Optional<Lending> findById(Long lendingId) {
        Optional<JpaLendingModel> jpaLending = Optional.ofNullable(em.find(JpaLendingModel.class, lendingId));  // Use find method to get Author by ID
        return jpaLending.map(lendingMapper::fromJpaLendingModel);
    }

}
