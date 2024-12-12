package pt.psoft.g1.psoftg1.suggestedbookmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.service.SuggestedBookService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class SuggestionEventRabbitmqReceiver {

    private final SuggestedBookService suggestedBookService;

    @Transactional
    @RabbitListener(queues = "#{autoDeleteQueue_Suggestion_Created.name}")
    public void receiveSuggestionCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            SuggestionViewAMQP suggestionViewAMQP = objectMapper.readValue(jsonReceived, SuggestionViewAMQP.class);

            System.out.println(" [x] Received Suggestion Created by AMQP: " + msg + ".");
            try {
                suggestedBookService.createSuggestedBook(suggestionViewAMQP);
                System.out.println(" [x] New Suggestion inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Suggested Book already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving Suggestion event from AMQP: '" + ex.getMessage() + "'");
        }
    }
}
