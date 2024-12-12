package pt.psoft.g1.psoftg1.suggestedbookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.dbSchema.JpaSuggestedBookModel;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.mapper.SuggestedBookMapper;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.repositories.SuggestedBookRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class SuggestedBookJpaRepoImpl implements SuggestedBookRepository {

    private final EntityManager em;
    private final SuggestedBookMapper suggestedBookMapper;

    @Override
    public SuggestedBook save(SuggestedBook suggestedBook) {
        JpaSuggestedBookModel jpaSuggestedBook = suggestedBookMapper.toJpaSuggestedBookModel(suggestedBook);

        if (suggestedBook.getPk() == null) {
            em.persist(jpaSuggestedBook);
        } else {
            em.merge(jpaSuggestedBook);
        }

        return suggestedBookMapper.fromJpaSuggestedBookModel(jpaSuggestedBook);
    }

    @Override
    public void delete(SuggestedBook suggestedBook) {
        JpaSuggestedBookModel jpaSuggestedBook = suggestedBookMapper.toJpaSuggestedBookModel(suggestedBook);

        if (em.contains(jpaSuggestedBook)) {
            em.remove(jpaSuggestedBook);  // If managed, remove directly
        } else {
            JpaSuggestedBookModel managedBook = em.merge(jpaSuggestedBook);  // If detached, merge to manage
            em.remove(managedBook);  // Then remove
        }
    }

    @Override
    public List<SuggestedBook> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaSuggestedBookModel> query = cb.createQuery(JpaSuggestedBookModel.class);
        query.from(JpaSuggestedBookModel.class);

        List<JpaSuggestedBookModel> jpaSuggestedBooks = em.createQuery(query).getResultList();

        return suggestedBookMapper.fromJpaSuggestedBookModel(jpaSuggestedBooks);
    }

    @Override
    public Optional<SuggestedBook> findById(String suggestedBookId) {
        Optional<JpaSuggestedBookModel> jpaSuggestedBook = Optional.ofNullable(em.find(JpaSuggestedBookModel.class, suggestedBookId));
        return jpaSuggestedBook.map(suggestedBookMapper::fromJpaSuggestedBookModel);
    }

    @Override
    public Optional<SuggestedBook> findByIsbn(String isbn) {
        String query = "SELECT b FROM JpaSuggestedBookModel b WHERE b.isbn = :isbn";
        JpaSuggestedBookModel jpaSuggestedBookModel = em.createQuery(query, JpaSuggestedBookModel.class)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return Optional.of(suggestedBookMapper.fromJpaSuggestedBookModel(jpaSuggestedBookModel));
    }
}
