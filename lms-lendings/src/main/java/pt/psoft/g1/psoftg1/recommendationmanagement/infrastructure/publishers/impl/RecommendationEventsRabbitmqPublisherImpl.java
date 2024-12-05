package pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationResponse;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQPMapper;
import pt.psoft.g1.psoftg1.recommendationmanagement.publishers.RecommendationEventPublisher;
import pt.psoft.g1.psoftg1.shared.model.RecommendationEvents;

@Service
@RequiredArgsConstructor
public class RecommendationEventsRabbitmqPublisherImpl implements RecommendationEventPublisher {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    @Qualifier("recommendationsExchange")
    private DirectExchange direct;
    private final RecommendationViewAMQPMapper recommendationViewAMQPMapper;

    @Override
    public String sendRecommendationCreated(String isbn, String readerNumber, boolean isPositive) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RecommendationViewAMQP recommendationViewAMQP =
                    recommendationViewAMQPMapper.toRecommendationViewAMQP(isbn, readerNumber, isPositive);

            String jsonString = objectMapper.writeValueAsString(recommendationViewAMQP);

            System.out.println(" [x] Sent RPC '" + recommendationViewAMQP + "'");
            String response = (String) this.template.convertSendAndReceive(direct.getName(), RecommendationEvents.RECOMMENDATION_RPC, jsonString);

            if (response == null || response.isEmpty()) {
                System.out.println(" [x] Received null response from RPC call.");
                return RecommendationResponse.RECOMMENDATION_FAILED;
            }

            System.out.println(" [x] Received '" + response + "' from RPC call.");

            return response;

        } catch (Exception ex) {
            System.out.println(" [x] Exception sending recommendation RPC: '" + ex.getMessage() + "'");
            return RecommendationResponse.RECOMMENDATION_FAILED;
        }
    }
}
