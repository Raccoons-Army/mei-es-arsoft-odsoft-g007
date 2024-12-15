package pt.psoft.g1.psoftg1.suggestedbookmanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;

import java.util.Optional;

@Repository
public interface SuggestedBookRepository extends CRUDRepository<SuggestedBook, String> {
    Optional<SuggestedBook> findByIsbn(String isbn);
}
