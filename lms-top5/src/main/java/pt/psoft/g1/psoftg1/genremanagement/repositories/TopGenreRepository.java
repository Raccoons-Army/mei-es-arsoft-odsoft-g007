package pt.psoft.g1.psoftg1.genremanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.model.TopGenre;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

@Repository
public interface TopGenreRepository extends CRUDRepository<TopGenre, String> {
    void deleteAll();
}
