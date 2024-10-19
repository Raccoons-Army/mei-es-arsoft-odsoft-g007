package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.RecommendationStrategy;

import java.util.List;

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
    private final AuthorRepository authorRepository;
    private final PhotoRepository photoRepository;
    private final ReaderRepository readerRepository;

    @Override
    public List<Book> getRecommendationsForReader(String readerNumber) {
       switch (strategy) {
           case AGE_BASED:
                return getRecommendationsAgeBased(readerNumber);
           case GENRE_BASED:
               return getRecommendationsGenreBased(readerNumber);
           default:
               return getRecommendationsAgeBased(readerNumber);
       }
    }

    public List<Book> getRecommendationsAgeBased(String readerNumber) {
        return null;
    }

    public List<Book> getRecommendationsGenreBased(String readerNumber) {
        return null;
    }
}
