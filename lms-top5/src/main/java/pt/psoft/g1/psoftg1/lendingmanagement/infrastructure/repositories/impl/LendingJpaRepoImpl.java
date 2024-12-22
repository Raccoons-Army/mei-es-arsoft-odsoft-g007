package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.LendingMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;

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
