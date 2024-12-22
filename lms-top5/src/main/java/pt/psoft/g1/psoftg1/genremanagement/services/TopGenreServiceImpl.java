package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.genremanagement.model.TopGenre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.TopGenreRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class TopGenreServiceImpl implements TopGenreService {

    @Value("${topGenres}")
    private int topGenresLimit;

    private final GenreRepository genreRepository;
    private final TopGenreRepository topGenreRepository;

    @Override
    public List<TopGenre> findTopX() {
        return topGenreRepository.findAll();
    }

    @Override
    @Transactional
    public void updateTopX() {
        Pageable pageableRules = PageRequest.of(0, topGenresLimit);
        // fetch top 5 most lent genres
        List<GenreCountDTO> topGenres = genreRepository.findTopXGenreByLendings(pageableRules).getContent();

        if(topGenres.isEmpty()) {
            return;
        }

        // Clear existing records
        topGenreRepository.deleteAll();

        // Save new top genres
        for (GenreCountDTO e : topGenres) {
            TopGenre topGenre = new TopGenre(e.getGenre(), e.getLendingCount());
            topGenreRepository.save(topGenre);
        }
    }
}
