package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.domain.PageImpl;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreCountDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;

@Transactional
@RequiredArgsConstructor
public class GenreJpaRepoImpl implements GenreRepository {

    private final EntityManager em;
    private final GenreMapper genreMapper;

    @Override
    public Optional<Genre> findByString(String genre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaGenreModel> query = cb.createQuery(JpaGenreModel.class);
        Root<JpaGenreModel> root = query.from(JpaGenreModel.class);

        query.select(root).where(cb.equal(root.get("genre"), genre));

        Optional<JpaGenreModel> jpaGenre = em.createQuery(query).getResultStream().findFirst();

        return jpaGenre.map(genreMapper::fromJpaGenre);
    }

    @Override
    public Genre save(Genre genre) {
        JpaGenreModel jpaGenre = genreMapper.toJpaGenre(genre);
        if (genre.getPk() == null) {
            em.persist(jpaGenre);
        } else {
            em.merge(jpaGenre);
        }
        return genreMapper.fromJpaGenre(jpaGenre);
    }

    @Override
    public void delete(Genre genre) {
        JpaGenreModel jpaGenre = genreMapper.toJpaGenre(genre);

        if (em.contains(jpaGenre)) {
            em.remove(jpaGenre);  // If managed, remove directly
        } else {
            JpaGenreModel managedGenre = em.merge(jpaGenre);  // If detached, merge to manage
            em.remove(managedGenre);  // Then remove
        }
    }

    @Override
    public Page<GenreCountDTO> findTopXGenreByLendings(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<JpaLendingModel> lendingRoot = query.from(JpaLendingModel.class);
        Join<JpaLendingModel, JpaBookModel> bookJoin = lendingRoot.join("book");
        Join<JpaBookModel, JpaGenreModel> genreJoin = bookJoin.join("genre");

        Expression<Long> countExpression = cb.count(lendingRoot);

        query.select(cb.tuple(genreJoin, countExpression))
                .groupBy(genreJoin)
                .orderBy(cb.desc(countExpression));

        TypedQuery<Tuple> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Tuple> resultTuples = typedQuery.getResultList();

        List<GenreCountDTO> topGenres = resultTuples.stream()
                .map(tuple -> new GenreCountDTO(
                        tuple.get(0, JpaGenreModel.class).getGenre(),
                        tuple.get(1, Long.class)))
                .toList();

        return new PageImpl<>(topGenres, pageable, topGenres.size());
    }

    @Override
    public List<Genre> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaGenreModel> query = cb.createQuery(JpaGenreModel.class);
        query.from(JpaGenreModel.class);

        List<JpaGenreModel> jpaGenres = em.createQuery(query).getResultList();
        return genreMapper.fromJpaGenre(jpaGenres);
    }

    @Override
    public Optional<Genre> findById(String genreId) {
        Optional<JpaGenreModel> jpaGenre = Optional.ofNullable(em.find(JpaGenreModel.class, genreId));  // Use find method to get Author by ID
        return jpaGenre.map(genreMapper::fromJpaGenre);
    }

}
