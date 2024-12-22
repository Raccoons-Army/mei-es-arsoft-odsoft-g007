package pt.psoft.g1.psoftg1.genremanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.Optional;

@Repository
public interface GenreRepository extends CRUDRepository<Genre, String> {

    Optional<Genre> findByString(String genreName);
    Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable);
    void delete(Genre genre);
}
