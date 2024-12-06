package pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.recommendationmanagement.dbschema.JpaRecommendationModel;
import pt.psoft.g1.psoftg1.recommendationmanagement.mapper.RecommendationMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RecommendationJpaRepoImpl implements RecommendationRepository {

    private final EntityManager em;
    private final RecommendationMapper recommendationMapper;


    @Override
    public Optional<Recommendation> findByBookIsbnAndReaderNumber(String bookIsbn, String readerNumber) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaRecommendationModel> cq = cb.createQuery(JpaRecommendationModel.class);
        Root<JpaRecommendationModel> root = cq.from(JpaRecommendationModel.class);

        cq.select(root)
                .where(cb.equal(root.get("bookIsbn"), bookIsbn),
                        cb.equal(root.get("readerNumber"), readerNumber));

        JpaRecommendationModel result;
        try {
            result = em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(recommendationMapper.fromJpaRecommendationModel(result));
    }

    @Override
    @Transactional
    public Recommendation save(Recommendation entity) {
        JpaRecommendationModel jpaRecommendationModel = recommendationMapper.toJpaRecommendationModel(entity);
        if (entity.getPk() == null) {
            em.persist(jpaRecommendationModel);
        } else {
            em.merge(jpaRecommendationModel);
        }
        return recommendationMapper.fromJpaRecommendationModel(jpaRecommendationModel);
    }

    @Override
    public void delete(Recommendation entity) {
        JpaRecommendationModel jpaRecommendationModel = recommendationMapper.toJpaRecommendationModel(entity);
        if (em.contains(jpaRecommendationModel)) {
            em.remove(jpaRecommendationModel);
        } else {
            em.remove(em.merge(jpaRecommendationModel));
        }
    }

    @Override
    public List<Recommendation> findAll() {
        return List.of();
    }

    @Override
    public Optional<Recommendation> findById(Long aLong) {
        return Optional.empty();
    }
}
