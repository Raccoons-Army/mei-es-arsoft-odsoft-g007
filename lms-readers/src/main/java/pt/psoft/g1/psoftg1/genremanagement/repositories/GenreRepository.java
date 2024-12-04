package pt.psoft.g1.psoftg1.genremanagement.repositories;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.Optional;

public interface GenreRepository extends CRUDRepository<Genre, String> {
    Optional<Genre> findByString(String genreName);
}
