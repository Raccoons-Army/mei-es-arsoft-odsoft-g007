package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;

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
        List<JpaReaderDetailsModel> list = em.createQuery(
                        "SELECT r FROM JpaReaderDetailsModel r WHERE r.phoneNumber = :phoneNumber",
                        JpaReaderDetailsModel.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();

        return readerDetailsMapper.fromJpaReaderDetailsModel(list);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        Optional<JpaReaderDetailsModel> m = em.createQuery(
                        "SELECT r FROM JpaReaderDetailsModel r WHERE r.username = :username",
                        JpaReaderDetailsModel.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();

        return m.map(readerDetailsMapper::fromJpaReaderDetailsModel);
    }

    @Override
    public int getCountFromCurrentYear() {
        Long count = (Long) em.createQuery(
                        "SELECT COUNT(rd) FROM JpaReaderDetailsModel rd JOIN JpaUserModel u ON rd.username = u.username WHERE YEAR(u.createdAt) = YEAR(CURRENT_DATE)")
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        List<JpaReaderDetailsModel> m = em.createQuery(
                        "SELECT rd FROM JpaReaderDetailsModel rd JOIN JpaLendingModel l ON l.readerDetails.pk = rd.pk GROUP BY rd ORDER BY COUNT(l) DESC",
                        JpaReaderDetailsModel.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long total = m.size();

        List<ReaderDetails> readerDetails = readerDetailsMapper.fromJpaReaderDetailsModel(m);

        return new PageImpl<>(readerDetails, pageable, total);
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(final pt.psoft.g1.psoftg1.shared.services.Page page, final SearchReadersQuery query) {

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<JpaReaderDetailsModel> cq = cb.createQuery(JpaReaderDetailsModel.class);
        final Root<JpaReaderDetailsModel> readerDetailsRoot = cq.from(JpaReaderDetailsModel.class);
        Join<JpaReaderDetailsModel, JpaUserModel> userJoin = readerDetailsRoot.join("reader");

        cq.select(readerDetailsRoot);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getName())) { //'contains' type search
            where.add(cb.like(userJoin.get("name"), "%" + query.getName() + "%"));
            cq.orderBy(cb.asc(userJoin.get("name")));
        }
        if (StringUtils.hasText(query.getEmail())) { //'exact' type search
            where.add(cb.equal(userJoin.get("username"), query.getEmail()));
            cq.orderBy(cb.asc(userJoin.get("username")));

        }
        if (StringUtils.hasText(query.getPhoneNumber())) { //'exact' type search
            where.add(cb.equal(readerDetailsRoot.get("phoneNumber"), query.getPhoneNumber()));
            cq.orderBy(cb.asc(readerDetailsRoot.get("phoneNumber")));
        }

        // search using OR
        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }


        final TypedQuery<JpaReaderDetailsModel> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return readerDetailsMapper.fromJpaReaderDetailsModel(q.getResultList());
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
        return readerDetailsMapper.fromJpaReaderDetailsModel(jpaReadersDetails);
    }

    @Override
    public Optional<ReaderDetails> findById(Long readerDetailsId) {
        Optional<JpaReaderDetailsModel> jpaReaderDetails = Optional.ofNullable(em.find(JpaReaderDetailsModel.class, readerDetailsId));  // Use find method to get Author by ID
        return jpaReaderDetails.map(readerDetailsMapper::fromJpaReaderDetailsModel);
    }
}
