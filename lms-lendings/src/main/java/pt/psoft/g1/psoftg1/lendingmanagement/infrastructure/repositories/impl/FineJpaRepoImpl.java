package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaFineModel;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.FineMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FineJpaRepoImpl implements FineRepository {

    private final EntityManager em;
    private final FineMapper fineMapper;

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaFineModel> query = cb.createQuery(JpaFineModel.class);
        Root<JpaFineModel> fine = query.from(JpaFineModel.class);
        Join<JpaFineModel, JpaLendingModel> lending = fine.join("lending");

        query.select(fine)
                .where(cb.equal(lending.get("lendingNumber"), lendingNumber));

        Optional<JpaFineModel> m = em.createQuery(query).getResultStream().findFirst();

        return m.map(fineMapper::fromJpaFineModel);
    }


    @Override
    public Iterable<Fine> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaFineModel> query = cb.createQuery(JpaFineModel.class);
        query.from(JpaFineModel.class); // Define the query to select from Fine

        List<JpaFineModel> list = em.createQuery(query).getResultList();

        return fineMapper.fromJpaFineModel(list);
    }

    @Override
    public Fine save(Fine fine) {
        JpaFineModel fineModel = fineMapper.toJpaFineModel(fine);
        if (fine.getPk() == null) {
            em.persist(fineModel);
        } else {
            em.merge(fineModel);
        }

        return fineMapper.fromJpaFineModel(fineModel);
    }
}
