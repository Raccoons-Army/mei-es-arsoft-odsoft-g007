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
