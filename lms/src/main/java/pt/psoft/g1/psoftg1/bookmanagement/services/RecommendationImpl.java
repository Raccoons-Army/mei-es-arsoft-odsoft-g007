package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.recommendationStrategies.RecommendationAgeBasedContext;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class RecommendationImpl implements RecommendationService {

    @Value("${recommendationBooksSize}")
    int nBooks;
    @Value("${recommendationGenreSize}")
    int nGenres;

    private final RecommendationStrategy strategy;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ReaderRepository readerRepository;

    private final RecommendationAgeBasedContext recommendationAgeBasedContext;

    @Override
    public List<Book> getRecommendationsForReader(String readerNumber) {
        ReaderDetails readerDetails = readerRepository.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        return switch (strategy) {
            case AGE_BASED -> getRecommendationsAgeBased(readerDetails);
            case GENRE_BASED -> getRecommendationsGenreBased();
        };
    }

    // method that returns a list of recommended books based on the readers age
    private List<Book> getRecommendationsAgeBased(ReaderDetails readerDetails) {
        // calc readers age
        int age = readerDetails.getBirthDate().getBirthDate().until(LocalDate.now()).getYears();

        return recommendationAgeBasedContext.getStrategy(age).recommend(readerDetails);
    }

    // method that returns a list of recommended books based on the genres most requested by the reader
    private List<Book> getRecommendationsGenreBased() {
        // return top genres by lendings
        List<Genre> genres = genreRepository.getTopYGenresMostLent(nGenres);

        // return x books from each genre
        List<Book> recommendedBooks = new ArrayList<>();

        for (Genre genre : genres) {
            List<Book> booksInGenre = bookRepository.findTopXBooksFromGenre(nBooks, genre.getGenre());
            recommendedBooks.addAll(booksInGenre);
        }
        // Return the list of recommended books (ensure distinct books if needed)
        return recommendedBooks.stream().distinct().collect(Collectors.toList());

    }
}
