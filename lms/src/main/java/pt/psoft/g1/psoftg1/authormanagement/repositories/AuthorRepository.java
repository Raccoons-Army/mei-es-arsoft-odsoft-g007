package pt.psoft.g1.psoftg1.authormanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorLendingView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends CRUDRepository<Author, Long> {

    List<Author> searchByNameNameStartsWith(String name);
    List<Author> searchByNameName(String name);
    Page<AuthorLendingView> findTopAuthorByLendings (Pageable pageableRules);
    List<Author> findCoAuthorsByAuthorNumber(Long authorNumber);
    Optional<Author> findByAuthorNumber(Long authorNumber);


}
