package cdc.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;

public class SuggestionMessageBuilder {

    private ObjectMapper mapper = new ObjectMapper();
    private SuggestionViewAMQP suggestionViewAMQP;

    public SuggestionMessageBuilder withSuggestion(SuggestionViewAMQP suggestionViewAMQP) {
        this.suggestionViewAMQP = suggestionViewAMQP;
        return this;
    }

    public Message<String> build() throws JsonProcessingException {
        return MessageBuilder.withPayload(this.mapper.writeValueAsString(this.suggestionViewAMQP))
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
