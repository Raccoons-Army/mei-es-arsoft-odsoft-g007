package cdc.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;

public class RecommendationMessageBuilder {

    private ObjectMapper mapper = new ObjectMapper();
    private RecommendationViewAMQP recommendationViewAMQP;

    public RecommendationMessageBuilder withRecommendation(RecommendationViewAMQP recommendationViewAMQP) {
        this.recommendationViewAMQP = recommendationViewAMQP;
        return this;
    }

    public Message<String> build() throws JsonProcessingException {
        return MessageBuilder.withPayload(this.mapper.writeValueAsString(this.recommendationViewAMQP))
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
