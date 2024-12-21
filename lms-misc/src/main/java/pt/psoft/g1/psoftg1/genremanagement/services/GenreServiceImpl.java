package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public Optional<Genre> findByString(String name) {
        return genreRepository.findByString(name);
    }

    @Override
    public List<GenreBookCountDTO> findTopGenreByBooks() {
        Pageable pageableRules = PageRequest.of(0, 5);
        return this.genreRepository.findTop5GenreByBookCount(pageableRules).getContent();
    }

    @Override
    public Genre create(GenreViewAMQP genre) {
        return this.genreRepository.save(new Genre(genre.getGenre()));
    }

    @Override
    public Genre update(GenreViewAMQP genre) {
        return null;
    }


}
