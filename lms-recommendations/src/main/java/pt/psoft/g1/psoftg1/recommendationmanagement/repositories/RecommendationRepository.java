package pt.psoft.g1.psoftg1.recommendationmanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends CRUDRepository<Recommendation, Long> {
    Optional<Recommendation> findByBookIsbnAndReaderNumber(String bookIsbn, String readerNumber);
}
