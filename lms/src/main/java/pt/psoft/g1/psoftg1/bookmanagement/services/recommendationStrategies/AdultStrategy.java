package pt.psoft.g1.psoftg1.bookmanagement.services.recommendationStrategies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class AdultStrategy implements RecommendationAgeBasedStrategy {

    @Value("${recommendationBooksSize}")
    int nBooks;

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<Book> recommend(ReaderDetails readerDetails) {
        // return books from the genre most requested by the reader
        Optional<Genre> genre = genreRepository.findReaderMostRequestedGenre(readerDetails.getReaderNumber());
        if (genre.isEmpty()) {
            // if there's no genre most requested by the reader, it's cuz he didn't request any book > return empty list
            return List.of();
        }

        List<Book> books = bookRepository.findByGenre(genre.get().getGenre());
        Collections.shuffle(books); // randomize the list of books
        return books.subList(0, Math.min(nBooks, books.size()));
    }
}
