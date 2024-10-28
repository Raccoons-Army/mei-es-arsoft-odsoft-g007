package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.lendingmanagement.mapper.FineMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class FineJpaRepoImpl implements FineRepository {

    private final EntityManager em;
    private final FineMapper fineMapper;

    @Override
    public Optional<Fine> findByLendingNumber(String lendingNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fine> query = cb.createQuery(Fine.class);
        Root<Fine> fine = query.from(Fine.class);
        Join<Fine, Lending> lending = fine.join("lending"); // Assuming 'lending' is the attribute name in Fine

        query.select(fine)
                .where(cb.equal(lending.get("lendingNumber").get("lendingNumber"), lendingNumber)); // Adjust path as necessary

        return em.createQuery(query).getResultStream().findFirst(); // Return the first result as Optional
    }


    @Override
    public Iterable<Fine> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Fine> query = cb.createQuery(Fine.class);
        query.from(Fine.class); // Define the query to select from Fine
        return em.createQuery(query).getResultList(); // Execute the query and return results
    }

    @Override
    public Fine save(Fine fine) {
        if (fine.getPk() == null) {
            em.persist(fine); // If it's a new entity, persist it
            return fine; // Return the persisted entity
        } else {
            return em.merge(fine); // If it's an existing entity, merge it
        }
    }
}
