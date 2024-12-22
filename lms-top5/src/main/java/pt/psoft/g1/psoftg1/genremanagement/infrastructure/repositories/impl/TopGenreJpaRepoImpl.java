package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaTopGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.TopGenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.TopGenre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.TopGenreRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class TopGenreJpaRepoImpl implements TopGenreRepository {

    private final EntityManager em;
    private final TopGenreMapper topGenreMapper;

    @Override
    public void deleteAll() {
        CriteriaDelete<JpaTopGenreModel> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(JpaTopGenreModel.class);
        criteriaDelete.from(JpaTopGenreModel.class);
        em.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public TopGenre save(TopGenre entity) {
        JpaTopGenreModel jpaTopGenreModel = topGenreMapper.toJpaTopGenre(entity);
        em.persist(jpaTopGenreModel);
        return topGenreMapper.fromJpaTopGenre(jpaTopGenreModel);
    }

    @Override
    public void delete(TopGenre entity) {
        CriteriaDelete<JpaTopGenreModel> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(JpaTopGenreModel.class);
        criteriaDelete.where(em.getCriteriaBuilder().equal(criteriaDelete.from(JpaTopGenreModel.class).get("genre"), entity.getGenre()));
        em.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public List<TopGenre> findAll() {
        List<JpaTopGenreModel> list = em.createQuery("SELECT b FROM JpaTopGenreModel b", JpaTopGenreModel.class)
                .getResultList();

        return topGenreMapper.fromJpaTopGenre(list);
    }

    @Override
    public Optional<TopGenre> findById(String s) {
        return Optional.empty();
    }
}
