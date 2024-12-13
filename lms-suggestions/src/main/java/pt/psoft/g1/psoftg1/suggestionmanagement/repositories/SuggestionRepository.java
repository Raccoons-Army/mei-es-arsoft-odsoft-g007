package pt.psoft.g1.psoftg1.suggestionmanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

import java.util.Optional;

@Repository
public interface SuggestionRepository extends CRUDRepository<Suggestion, String> {

    Optional<Suggestion> findByIsbn(String isbn);
}
