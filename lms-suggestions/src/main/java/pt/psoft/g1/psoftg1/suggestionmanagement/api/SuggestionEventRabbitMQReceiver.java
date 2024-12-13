package pt.psoft.g1.psoftg1.suggestionmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class SuggestionEventRabbitMQReceiver {

    private final SuggestionService suggestionService;

    @Transactional
    @RabbitListener(queues = "#{autoDeleteQueue_Suggestion_Created.name}")
    public void receiveSuggestionCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            SuggestionViewAMQP suggestionViewAMQP = objectMapper.readValue(jsonReceived, SuggestionViewAMQP.class);

            System.out.println(" [x] Received Suggestion Created by AMQP: " + msg + ".");
            try {
                suggestionService.createSuggestion(suggestionViewAMQP);
                System.out.println(" [x] New Suggestion inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Suggestion already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving Suggestion event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @Transactional
    @RabbitListener(queues = "#{autoDeleteQueue_Suggestion_Updated.name}")
    public void receiveSuggestionUpdated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            SuggestionViewAMQP suggestionViewAMQP = objectMapper.readValue(jsonReceived, SuggestionViewAMQP.class);

            System.out.println(" [x] Received Suggestion Updated by AMQP: " + msg + ".");
            try {
                suggestionService.updateSuggestion(suggestionViewAMQP);
                System.out.println(" [x] Suggestion updated from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Suggestion does not exists or wrong version. Nothing stored.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving Suggestion event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @Transactional
    @RabbitListener(queues = "#{autoDeleteQueue_Suggestion_Deleted.name}")
    public void receiveSuggestionDeleted(String in) {
        System.out.println(" [x] Received Suggestion Deleted '" + in + "'");
    }
}
