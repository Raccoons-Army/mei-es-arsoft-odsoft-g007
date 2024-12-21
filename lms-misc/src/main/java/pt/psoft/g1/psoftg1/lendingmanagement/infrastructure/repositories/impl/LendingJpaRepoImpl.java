package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
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
        CriteriaQuery<JpaLendingModel> query = cb.createQuery(JpaLendingModel.class);
        Root<JpaLendingModel> lendingRoot = query.from(JpaLendingModel.class);
        Join<JpaLendingModel, JpaBookModel> bookJoin = lendingRoot.join("book");
        Join<JpaLendingModel, JpaReaderDetailsModel> readerJoin = lendingRoot.join("readerDetails");

        // Build the where clause
        query.select(lendingRoot)
                .where(cb.and(
                        cb.equal(bookJoin.get("isbn"), isbn),
                        cb.equal(readerJoin.get("readerNumber"), readerNumber)
                ));

        List<JpaLendingModel> list = em.createQuery(query).getResultList();
        return lendingMapper.fromJpaLendingModel(list);
    }

    @Override
    public Double getAverageDuration() {
        // Using native query to calculate average duration
        return (Double) em.createNativeQuery(
                        "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) FROM JpaLendingModel l")
                .getSingleResult();
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        // Using native query to calculate average duration by ISBN
        return (Double) em.createNativeQuery(
                        "SELECT AVG(DATEDIFF(day, l.start_date, l.returned_date)) " +
                                "FROM JpaLendingModel l " +
                                "JOIN JpaBookModel b ON l.BOOK_PK = b.PK " +
                                "WHERE b.ISBN = :isbn")
                .setParameter("isbn", isbn)
                .getSingleResult();
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<JpaLendingModel> cq = cb.createQuery(JpaLendingModel.class);
        final Root<JpaLendingModel> root = cq.from(JpaLendingModel.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();

        // Select overdue lendings where returnedDate is null and limitDate is before the current date
        where.add(cb.isNull(root.get("returnedDate")));
        where.add(cb.lessThan(root.get("limitDate"), LocalDate.now()));

        cq.where(where.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("limitDate"))); // Order by limitDate, oldest first

        final TypedQuery<JpaLendingModel> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        List<JpaLendingModel> list = q.getResultList();
        return lendingMapper.fromJpaLendingModel(list);
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
