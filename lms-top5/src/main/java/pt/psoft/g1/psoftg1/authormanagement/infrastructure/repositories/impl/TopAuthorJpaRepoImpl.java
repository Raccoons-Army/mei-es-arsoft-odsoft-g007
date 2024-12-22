package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaTopAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.mapper.TopAuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.TopAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.TopAuthorRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class TopAuthorJpaRepoImpl implements TopAuthorRepository {

    private final EntityManager em;
    private final TopAuthorMapper topAuthorMapper;

    @Override
    public void deleteAll() {
        CriteriaDelete<JpaTopAuthorModel> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(JpaTopAuthorModel.class);
        criteriaDelete.from(JpaTopAuthorModel.class);
        em.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public TopAuthor save(TopAuthor entity) {
        JpaTopAuthorModel jpaTopAuthorModel = topAuthorMapper.toJpaTopAuthor(entity);
        em.persist(jpaTopAuthorModel);
        return topAuthorMapper.fromJpaTopAuthor(jpaTopAuthorModel);
    }

    @Override
    public void delete(TopAuthor entity) {
        CriteriaDelete<JpaTopAuthorModel> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(JpaTopAuthorModel.class);
        criteriaDelete.where(em.getCriteriaBuilder().equal(criteriaDelete.from(JpaTopAuthorModel.class).get("name"), entity.getName()));
        em.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public List<TopAuthor> findAll() {
        List<JpaTopAuthorModel> list = em.createQuery("SELECT b FROM JpaTopAuthorModel b", JpaTopAuthorModel.class)
                .getResultList();

        return topAuthorMapper.fromJpaTopAuthor(list);
    }

    @Override
    public Optional<TopAuthor> findById(String s) {
        return Optional.empty();
    }
}
