package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.publishers.RecommendationEventPublisher;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    private final FactoryBook factoryBook;
    private final FactoryReaderDetails factoryReaderDetails;

    private final RecommendationEventPublisher recommendationEventPublisher;

    /**
     * we only want to propagate the event if the resource came from a RPC call or else we will have an infinite loop of events
     * and we only need to return a boolean to the RPC call as well
     **/
    @Override
    public void createThroughRPC(RecommendationViewAMQP resource) {
        // update recommendation if it already exists
        Optional<Recommendation> recommendationOptional = recommendationRepository.findByBookIsbnAndReaderNumber(
                resource.getIsbn(), resource.getReaderNumber());
        if (recommendationOptional.isPresent()) {
            Recommendation recommendation = recommendationOptional.get();
            recommendation.setPositive(resource.isPositive());
            recommendationRepository.save(recommendation);

            recommendationEventPublisher.sendRecommendationUpdated(recommendation, recommendation.getVersion());
            return;
        }

        Recommendation recommendation = createNewRecommendation(resource);
        recommendationEventPublisher.sendRecommendationCreated(recommendation);
    }

    @Override
    public void create(RecommendationViewAMQP resource) {
        // update recommendation if it already exists
        Optional<Recommendation> recommendationOptional = recommendationRepository.findByBookIsbnAndReaderNumber(
                resource.getIsbn(), resource.getReaderNumber());
        if (recommendationOptional.isPresent()) {
            throw new ConflictException("Recommendation already exists");
        }

        createNewRecommendation(resource);
    }

    private Recommendation createNewRecommendation(RecommendationViewAMQP resource) {
        final Recommendation recommendation = new Recommendation(factoryBook, factoryReaderDetails, resource.isPositive());
        Book b = bookRepository.findByIsbn(resource.getIsbn()).orElseThrow(() -> new ConflictException(
                "Book with isbn " + resource.getIsbn() + " not found"));
        recommendation.defineBook(b);
        ReaderDetails r = readerRepository.findByReaderNumber(resource.getReaderNumber()).orElseThrow(() ->
                new ConflictException("Reader with number " + resource.getReaderNumber() + " not found"));
        recommendation.defineReaderDetails(r);

        return recommendationRepository.save(recommendation);
    }

    @Override
    public void update(RecommendationViewAMQP resource) {
        // update recommendation if it already exists
        Optional<Recommendation> recommendationOptional = recommendationRepository.findByBookIsbnAndReaderNumber(
                resource.getIsbn(), resource.getReaderNumber());
        if (recommendationOptional.isEmpty()) {
            throw new NotFoundException("Recommendation not found");
        }
        Recommendation recommendation = recommendationOptional.get();
        recommendation.setPositive(resource.isPositive());
        recommendationRepository.save(recommendation);
    }
}
