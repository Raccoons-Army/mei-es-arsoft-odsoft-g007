package pt.psoft.g1.psoftg1.suggestionmanagement.infraestructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.suggestionmanagement.dbSchema.JpaSuggestionModel;
import pt.psoft.g1.psoftg1.suggestionmanagement.mapper.SuggestionMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class SuggestionJpaRepoImpl implements SuggestionRepository {

    private final SuggestionMapper suggestionMapper;
    private final EntityManager em;

    @Override
    public Suggestion save(Suggestion suggestion) {
        JpaSuggestionModel jpaSuggestion = suggestionMapper.toJpaSuggestionModel(suggestion);
        if (suggestion.getVersion() == 0) {
            em.persist(jpaSuggestion);
        } else {
            em.merge(jpaSuggestion);
        }
        return suggestionMapper.fromJpaSuggestionModel(jpaSuggestion);
    }

    @Override
    public void delete(Suggestion suggestion) {
        JpaSuggestionModel jpaSuggestion = suggestionMapper.toJpaSuggestionModel(suggestion);

        if (em.contains(jpaSuggestion)) {
            em.remove(jpaSuggestion);  // If managed, remove directly
        } else {
            JpaSuggestionModel managedSuggestion = em.merge(jpaSuggestion);  // If detached, merge to manage
            em.remove(managedSuggestion);  // Then remove
        }
    }

    @Override
    public List<Suggestion> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JpaSuggestionModel> query = cb.createQuery(JpaSuggestionModel.class);
        query.from(JpaSuggestionModel.class);

        List<JpaSuggestionModel> jpaSuggestions = em.createQuery(query).getResultList();
        return suggestionMapper.fromJpaSuggestionModel(jpaSuggestions);
    }

    @Override
    public Optional<Suggestion> findById(String suggestionId) {
        Optional<JpaSuggestionModel> jpaSuggestion = Optional.ofNullable(em.find(JpaSuggestionModel.class, suggestionId));
        return jpaSuggestion.map(suggestionMapper::fromJpaSuggestionModel);
    }

    @Override
    public Optional<Suggestion> findByIsbn(String isbn) {
        TypedQuery<JpaSuggestionModel> query = em.createQuery(
                "SELECT s FROM JpaSuggestionModel s WHERE s.suggestedBook = :isbn", JpaSuggestionModel.class);
        query.setParameter("isbn", isbn);

        Optional<JpaSuggestionModel> jpaSuggestion = query.getResultStream().findFirst();
        return jpaSuggestion.map(suggestionMapper::fromJpaSuggestionModel);
    }


}
