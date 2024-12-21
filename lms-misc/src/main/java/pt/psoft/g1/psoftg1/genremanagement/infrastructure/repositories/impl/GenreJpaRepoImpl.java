package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.*;

import org.springframework.data.domain.PageImpl;

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
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GenreBookCountDTO> query = cb.createQuery(GenreBookCountDTO.class);
        Root<JpaGenreModel> genreRoot = query.from(JpaGenreModel.class);
        Join<JpaGenreModel, JpaBookModel> bookJoin = genreRoot.join("books");

        // Create selection for GenreBookCountDTO
        query.select(cb.construct(GenreBookCountDTO.class, genreRoot.get("genre"), cb.count(bookJoin)))
                .groupBy(genreRoot).orderBy(cb.desc(cb.count(bookJoin)));

        TypedQuery<GenreBookCountDTO> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<GenreBookCountDTO> results = typedQuery.getResultList();

        long total = results.size();

        return new PageImpl<>(results, pageable, total);
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
