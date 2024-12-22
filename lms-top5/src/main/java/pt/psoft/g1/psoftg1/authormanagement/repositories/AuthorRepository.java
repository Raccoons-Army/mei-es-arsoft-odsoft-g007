package pt.psoft.g1.psoftg1.authormanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorCountView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CRUDRepository<Author, String> {
    List<Author> searchByNameNameStartsWith(String name);
    List<Author> searchByNameName(String name);
    List<Author> findCoAuthorsByAuthorNumber(String authorNumber);
    Optional<Author> findByAuthorNumber(String authorNumber);
    Page<AuthorCountView> findTopXAuthorByLendings (Pageable pageableRules);

}
