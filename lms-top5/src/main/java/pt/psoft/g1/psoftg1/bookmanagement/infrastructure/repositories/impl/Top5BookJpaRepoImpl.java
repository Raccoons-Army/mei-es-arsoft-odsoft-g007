package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaTopBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.TopBookMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.Top5BookRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class Top5BookJpaRepoImpl implements Top5BookRepository {

    private final EntityManager em;
    private final TopBookMapper topBookMapper;

    @Override
    public void deleteAll() {
        CriteriaDelete<JpaTopBookModel> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(JpaTopBookModel.class);
        criteriaDelete.from(JpaTopBookModel.class);
        em.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public TopBook save(TopBook entity) {
        JpaTopBookModel jpaTopBookModel = topBookMapper.toJpaTopBookModel(entity);
        em.persist(jpaTopBookModel);
        return topBookMapper.fromJpaTopBookModel(jpaTopBookModel);
    }

    @Override
    public void delete(TopBook entity) {
        CriteriaDelete<JpaTopBookModel> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(JpaTopBookModel.class);
        criteriaDelete.where(em.getCriteriaBuilder().equal(criteriaDelete.from(JpaTopBookModel.class).get("isbn"), entity.getIsbn()));
        em.createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public List<TopBook> findAll() {
        List<JpaTopBookModel> list = em.createQuery("SELECT b FROM JpaTopBookModel b", JpaTopBookModel.class)
                .getResultList();

        return topBookMapper.fromJpaTopBookModel(list);
    }

    @Override
    public Optional<TopBook> findById(String s) {
        return Optional.empty();
    }
}
