package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;

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
        recommendation.defineBook(resource.getIsbn());
        recommendation.defineReaderDetails(resource.getReaderNumber());

        return recommendationRepository.save(recommendation) != null;
    }
}
