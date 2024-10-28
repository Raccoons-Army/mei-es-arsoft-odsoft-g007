package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.mapper.ReaderMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReaderJpaRepoImpl implements ReaderRepository {

    private final EntityManager em;
    private final ReaderDetailsMapper readerDetailsMapper;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {// Create CriteriaBuilder
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaReaderDetailsModel> cq = cb.createQuery(JpaReaderDetailsModel.class);
        Root<JpaReaderDetailsModel> root = cq.from(JpaReaderDetailsModel.class);
        cq.where(cb.equal(root.get("readerNumber"), readerNumber));

        // Execute query
        Optional<JpaReaderDetailsModel> readerDetails = em.createQuery(cq).getResultList().stream().findFirst();

        return readerDetails.map(readerDetailsMapper::fromJpaReaderDetailsModel);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return em.createQuery(
                        "SELECT r FROM ReaderDetails r WHERE r.phoneNumber.phoneNumber = :phoneNumber",
                        ReaderDetails.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        return em.createQuery(
                        "SELECT r FROM ReaderDetails r JOIN User u ON r.reader.id = u.id WHERE u.username = :username",
                        ReaderDetails.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Optional<ReaderDetails> findByUserId(Long userId) {
        return em.createQuery(
                        "SELECT r FROM ReaderDetails r JOIN User u ON r.reader.id = u.id WHERE u.id = :userId",
                        ReaderDetails.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public int getCountFromCurrentYear() {
        Long count = (Long) em.createQuery(
                        "SELECT COUNT(rd) FROM ReaderDetails rd JOIN User u ON rd.reader.id = u.id WHERE YEAR(u.createdAt) = YEAR(CURRENT_DATE)")
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        List<ReaderDetails> readerDetails = em.createQuery(
                        "SELECT rd FROM ReaderDetails rd JOIN Lending l ON l.readerDetails.pk = rd.pk GROUP BY rd ORDER BY COUNT(l) DESC",
                        ReaderDetails.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = readerDetails.size();

        return new PageImpl<>(readerDetails, pageable, total);
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        List<ReaderBookCountDTO> results = em.createQuery(
                        "SELECT NEW pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO(rd, COUNT(l)) " +
                                "FROM ReaderDetails rd JOIN Lending l ON l.readerDetails.pk = rd.pk " +
                                "JOIN Book b ON b.pk = l.book.pk " +
                                "JOIN Genre g ON g.pk = b.genre.pk " +
                                "WHERE g.genre = :genre AND l.startDate >= :startDate AND l.startDate <= :endDate " +
                                "GROUP BY rd.pk ORDER BY COUNT(l.pk) DESC",
                        ReaderBookCountDTO.class)
                .setParameter("genre", genre)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = results.size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(final pt.psoft.g1.psoftg1.shared.services.Page page, final SearchReadersQuery query) {

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<ReaderDetails> cq = cb.createQuery(ReaderDetails.class);
        final Root<ReaderDetails> readerDetailsRoot = cq.from(ReaderDetails.class);
        Join<ReaderDetails, User> userJoin = readerDetailsRoot.join("reader");

        cq.select(readerDetailsRoot);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getName())) { //'contains' type search
            where.add(cb.like(userJoin.get("name").get("name"), "%" + query.getName() + "%"));
            cq.orderBy(cb.asc(userJoin.get("name")));
        }
        if (StringUtils.hasText(query.getEmail())) { //'exatct' type search
            where.add(cb.equal(userJoin.get("username"), query.getEmail()));
            cq.orderBy(cb.asc(userJoin.get("username")));

        }
        if (StringUtils.hasText(query.getPhoneNumber())) { //'exatct' type search
            where.add(cb.equal(readerDetailsRoot.get("phoneNumber").get("phoneNumber"), query.getPhoneNumber()));
            cq.orderBy(cb.asc(readerDetailsRoot.get("phoneNumber").get("phoneNumber")));
        }

        // search using OR
        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }


        final TypedQuery<ReaderDetails> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        JpaReaderDetailsModel jpaReaderDetails = readerDetailsMapper.toJpaReaderDetailsModel(readerDetails);
        if (readerDetails.getPk() == null) {
            em.persist(jpaReaderDetails);
        } else {
            em.merge(jpaReaderDetails);
        }
        return readerDetailsMapper.fromJpaReaderDetailsModel(jpaReaderDetails);
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        JpaReaderDetailsModel jpaReaderDetails = readerDetailsMapper.toJpaReaderDetailsModel(readerDetails);

        if (em.contains(jpaReaderDetails)) {
            em.remove(jpaReaderDetails);  // If managed, remove directly
        } else {
            JpaReaderDetailsModel managedReaderDetails = em.merge(jpaReaderDetails);  // If detached, merge to manage
            em.remove(managedReaderDetails);  // Then remove
        }
    }

    @Override
    public List<ReaderDetails> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaReaderDetailsModel> query = cb.createQuery(JpaReaderDetailsModel.class);
        query.from(JpaReaderDetailsModel.class);

        List<JpaReaderDetailsModel> jpaReadersDetails = em.createQuery(query).getResultList();
        List<ReaderDetails> readerDetails = new ArrayList<>();
        for (JpaReaderDetailsModel i : jpaReadersDetails) {
            readerDetails.add(readerDetailsMapper.fromJpaReaderDetailsModel(i));
        }
        return readerDetails;
    }

    @Override
    public Optional<ReaderDetails> findById(Long readerDetailsId) {
        Optional<JpaReaderDetailsModel> jpaReaderDetails = Optional.ofNullable(em.find(JpaReaderDetailsModel.class, readerDetailsId));  // Use find method to get Author by ID
        return jpaReaderDetails.map(readerDetailsMapper::fromJpaReaderDetailsModel);
    }
}
