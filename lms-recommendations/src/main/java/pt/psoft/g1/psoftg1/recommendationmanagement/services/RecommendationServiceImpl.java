package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    private final FactoryBook factoryBook;
    private final FactoryReaderDetails factoryReaderDetails;

    @Override
    public boolean create(RecommendationViewAMQP resource) {
        // TODO: ask to the client if a reader can recommend a book more than once
//        if (recommendationRepository.findByBookIsbnAndReaderNumber(resource.getIsbn(),
//                resource.getReaderNumber()).isPresent()) {
//            throw new ConflictException("Recommendation from reader with number " + resource.getReaderNumber() + " for book with isbn " +
//                    resource.getIsbn() + " already exists");
//        }

        final Recommendation recommendation = new Recommendation(factoryBook, factoryReaderDetails, resource.isPositive());
        Book b = bookRepository.findByIsbn(resource.getIsbn()).orElseThrow(() -> new ConflictException(
                "Book with isbn " + resource.getIsbn() + " not found"));
        recommendation.defineBook(b);
        ReaderDetails r = readerRepository.findByReaderNumber(resource.getReaderNumber()).orElseThrow(() ->
                new ConflictException("Reader with number " + resource.getReaderNumber() + " not found"));
        recommendation.defineReaderDetails(r);

        return recommendationRepository.save(recommendation) != null;
    }
}
