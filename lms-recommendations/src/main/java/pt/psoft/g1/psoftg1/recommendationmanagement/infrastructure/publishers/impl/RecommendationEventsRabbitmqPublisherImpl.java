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
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
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
    public RecommendationViewAMQP sendRecommendationCreated(Recommendation recommendation) {
        return sendRecommendationEvent(recommendation, recommendation.getVersion(), RecommendationEvents.RECOMMENDATION_CREATED);
    }

    @Override
    public void sendRecommendationUpdated(Recommendation recommendation, Long currentVersion) {
        sendRecommendationEvent(recommendation, currentVersion, RecommendationEvents.RECOMMENDATION_UPDATED);
    }

    private RecommendationViewAMQP sendRecommendationEvent(Recommendation recommendation, long version, String recommendationEventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            RecommendationViewAMQP recommendationViewAMQP = recommendationViewAMQPMapper.toRecommendationViewAMQP(recommendation);
            recommendationViewAMQP.setVersion(version);

            String jsonString = objectMapper.writeValueAsString(recommendationViewAMQP);

            this.template.convertAndSend(direct.getName(), recommendationEventType, jsonString);

            System.out.println(" [x] Sent '" + recommendationViewAMQP + "'");

            return recommendationViewAMQP;

        } catch (Exception ex) {
            System.out.println(" [x] Exception sending recommendation event: '" + ex.getMessage() + "'");
            return null;
        }
    }
}
