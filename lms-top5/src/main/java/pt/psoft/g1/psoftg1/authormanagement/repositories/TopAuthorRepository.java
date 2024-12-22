package pt.psoft.g1.psoftg1.authormanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.model.TopAuthor;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

@Repository
public interface TopAuthorRepository extends CRUDRepository<TopAuthor, String> {
    void deleteAll();
}
