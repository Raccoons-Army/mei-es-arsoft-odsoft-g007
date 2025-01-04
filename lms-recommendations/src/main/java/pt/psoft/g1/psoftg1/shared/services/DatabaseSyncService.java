package pt.psoft.g1.psoftg1.shared.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQPMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseSyncService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RecommendationViewAMQPMapper recommendationViewAMQP;

    @RabbitListener(queues = RabbitmqClientConfig.RECOMMENDATION_DB_SYNC_QUEUE)
    public String handleRecommendationDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<Recommendation> allData = recommendationRepository.findAll();

            // Convert the DTOs to JSON for transfer
            List<RecommendationViewAMQP> recommendationsAmqp = recommendationViewAMQP.toRecommendationViewAMQP(allData);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(recommendationsAmqp);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }
}

