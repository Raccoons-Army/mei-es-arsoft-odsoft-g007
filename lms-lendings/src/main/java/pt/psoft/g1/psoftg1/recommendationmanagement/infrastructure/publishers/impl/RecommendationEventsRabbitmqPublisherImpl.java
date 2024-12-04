package pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
    public void sendRecommendationCreated(String isbn, String readerNumber, boolean isPositive) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RecommendationViewAMQP recommendationViewAMQP =
                    recommendationViewAMQPMapper.toRecommendationViewAMQP(isbn, readerNumber, isPositive);

            String jsonString = objectMapper.writeValueAsString(recommendationViewAMQP);

            this.template.convertAndSend(direct.getName(), RecommendationEvents.RECOMMENDATION_CREATED, jsonString);

            System.out.println(" [x] Sent '" + recommendationViewAMQP + "'");
        } catch (Exception ex) {
            System.out.println(" [x] Exception sending recommendation event: '" + ex.getMessage() + "'");
        }
    }
}
