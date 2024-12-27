package pt.psoft.g1.psoftg1.suggestionmanagement.infraestructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pt.psoft.g1.psoftg1.shared.model.SuggestionEvents;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQPMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.publishers.SuggestionEventPublisher;

@Service
@RequiredArgsConstructor
public class SuggestionEventsRabbitMQPublisherImpl implements SuggestionEventPublisher {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    @Qualifier("suggestionsExchange")
    private DirectExchange direct;

    private final SuggestionViewAMQPMapper suggestionViewAMQPMapper;

    @Override
    public SuggestionViewAMQP sendSuggestionCreated(Suggestion suggestion) {
        return sendSuggestionEvent(suggestion, suggestion.getVersion(), SuggestionEvents.SUGGESTION_CREATED);
    }

    @Override
    public SuggestionViewAMQP sendSuggestionUpdated(Suggestion suggestion, Long currentVersion) {
        return sendSuggestionEvent(suggestion, currentVersion, SuggestionEvents.SUGGESTION_UPDATED);
    }

    @Override
    public SuggestionViewAMQP sendSuggestionDeleted(Suggestion suggestion, Long currentVersion) {
        return sendSuggestionEvent(suggestion, currentVersion, SuggestionEvents.SUGGESTION_DELETED);
    }

    private SuggestionViewAMQP sendSuggestionEvent(Suggestion suggestion, long version, String suggestionEventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            SuggestionViewAMQP suggestionViewAMQP = suggestionViewAMQPMapper.toSuggestionViewAMQP(suggestion);
            suggestionViewAMQP.setVersion(version);

            String jsonString = objectMapper.writeValueAsString(suggestionViewAMQP);

            this.template.convertAndSend(direct.getName(), suggestionEventType, jsonString);

            System.out.println(" [x] Sent '" + suggestionViewAMQP + "'");
            return suggestionViewAMQP;
        } catch (Exception ex) {
            System.out.println(" [x] Exception sending suggestion event: '" + ex.getMessage() + "'");
            return null;
        }
    }
}
