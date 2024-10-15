package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
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

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fine> query = cb.createQuery(Fine.class);
        Root<Fine> fineRoot = query.from(Fine.class);
        Join<Fine, Lending> lendingJoin = fineRoot.join("lending"); // Assuming a relationship to Lending

        // Assuming Lending has a property called lendingNumber that is an object with the field lendingNumber
        query.select(fineRoot)
                .where(cb.equal(lendingJoin.get("lendingNumber").get("lendingNumber"), lendingNumber));

        return em.createQuery(query)
                .setMaxResults(1) // Limit to one result
                .getResultStream() // Convert results to stream
                .findFirst(); // Retrieve the first element as Optional
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
    public Lending save(Lending entity) {
        return em.merge(entity); // Use merge to save or update the entity
    }

    @Override
    public void delete(Lending entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    public List<Lending> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lending> query = cb.createQuery(Lending.class);
        query.from(Lending.class);
        return em.createQuery(query).getResultList(); // Return the list of all Lending entities
    }

    @Override
    public Lending findById(Long id) {
        return em.find(Lending.class, id); // Use find to retrieve the entity by ID
    }

}
