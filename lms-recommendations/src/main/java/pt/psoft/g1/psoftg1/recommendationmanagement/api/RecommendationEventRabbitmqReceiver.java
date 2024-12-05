package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RecommendationEventRabbitmqReceiver {

    private final RecommendationService recommendationService;

    @RabbitListener(queues = "#{autoDeleteQueue_Recommendation_Created.name}}")
    public void receiveRecommendationCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            RecommendationViewAMQP recommendationViewAMQP = objectMapper.readValue(jsonReceived, RecommendationViewAMQP.class);

            System.out.println(" [x] Received Recommendation Created by AMQP: " + msg + ".");
            try {
                recommendationService.create(recommendationViewAMQP);
                System.out.println(" [x] New recommendation inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Recommendation already exists. No need to store it.");
            }
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving recommendation event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    // rpc listener
    @RabbitListener(queues = "#{autoDeleteQueue_Recommendation_Rpc.name}}")
    public String receiveRecommendationRpc(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            RecommendationViewAMQP recommendationViewAMQP = objectMapper.readValue(jsonReceived, RecommendationViewAMQP.class);

            System.out.println(" [x] Received Recommendation RPC by AMQP: " + msg + ".");
            try {
                if (recommendationService.create(recommendationViewAMQP)) {
                    System.out.println(" [x] New recommendation inserted from AMQP: " + msg + ".");
                    return RecommendationResponse.RECOMMENDATION_CREATED;
                }

                System.out.println(" [x] Recommendation failed to be inserted from AMQP: " + msg + ".");
                return RecommendationResponse.RECOMMENDATION_FAILED;

            } catch (Exception e) {
                System.out.println(" [x] Recommendation already exists. No need to store it.");
                return RecommendationResponse.RECOMMENDATION_EXISTS;
            }
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving recommendation event from AMQP: '" + ex.getMessage() + "'");
            return RecommendationResponse.RECOMMENDATION_FAILED;
        }
    }
}
