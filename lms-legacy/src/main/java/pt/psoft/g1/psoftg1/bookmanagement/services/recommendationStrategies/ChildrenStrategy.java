package pt.psoft.g1.psoftg1.bookmanagement.services.recommendationStrategies;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class ChildrenStrategy implements RecommendationAgeBasedStrategy {

    @Value("${recommendationBooksSize}")
    int nBooks;
    @Value("${childrenGenre}")
    String childrenGenre;

    private final BookRepository bookRepository;

    @Override
    public List<Book> recommend(ReaderDetails readerDetails) {
        List<Book> books = bookRepository.findByGenre(childrenGenre);
        Collections.shuffle(books); // randomize the list of books
        return books.subList(0, Math.min(nBooks, books.size()));
    }
}
