package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ForbiddenNameJpaRepoImpl implements ForbiddenNameRepository {

    private final EntityManager em;

    @Override
    public Iterable<ForbiddenName> findAll() {
        return null;
    }

    @Override
    public List<ForbiddenName> findByForbiddenNameIsContained(String pat) {
        String pattern = "%" + pat + "%";
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ForbiddenName> query = cb.createQuery(ForbiddenName.class);
        Root<ForbiddenName> root = query.from(ForbiddenName.class);

        query.select(root)
                .where(cb.like(root.get("forbiddenName"), pattern));

        return em.createQuery(query).getResultList();
    }

    @Override
    public ForbiddenName save(ForbiddenName forbiddenName) {
        return null;
    }

    @Override
    public Optional<ForbiddenName> findByForbiddenName(String forbiddenName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ForbiddenName> query = cb.createQuery(ForbiddenName.class);
        Root<ForbiddenName> root = query.from(ForbiddenName.class);

        query.select(root)
                .where(cb.equal(root.get("forbiddenName"), forbiddenName));

        return em.createQuery(query)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Modifying
    @Query("DELETE FROM ForbiddenName fn WHERE fn.forbiddenName = :forbiddenName")
    public int deleteForbiddenName(String forbiddenName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<ForbiddenName> delete = cb.createCriteriaDelete(ForbiddenName.class);
        Root<ForbiddenName> root = delete.from(ForbiddenName.class);

        delete.where(cb.equal(root.get("forbiddenName"), forbiddenName));

        return em.createQuery(delete).executeUpdate();
    }
}
