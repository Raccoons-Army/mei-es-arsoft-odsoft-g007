package pt.psoft.g1.psoftg1.authormanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CRUDRepository<Author, String> {
    List<Author> findCoAuthorsByAuthorNumber(String authorNumber);
    Optional<Author> findByAuthorNumber(String authorNumber);
}
