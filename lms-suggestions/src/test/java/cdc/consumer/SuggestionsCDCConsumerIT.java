package cdc.consumer;

import au.com.dius.pact.core.model.*;
import au.com.dius.pact.core.model.messaging.Message;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {SuggestionEventRabbitMQReceiver.class, SuggestionService.class}
)
public class SuggestionsCDCConsumerIT {

    @MockBean
    SuggestionService suggestionService;

    @Autowired
    SuggestionEventRabbitMQReceiver listener;

    @Test
    void testMessageProcessing() throws Exception {

        // Use PactReader to load the Pact file
        File pactFile = new File("target/pacts/suggestion_created-consumer-suggestion_event-producer.json");
        PactReader pactReader = DefaultPactReader.INSTANCE;

        Pact pact = pactReader.loadPact(pactFile);

        List<Message> messagesGeneratedByPact = pact.asMessagePact().get().getMessages();
        for (Message messageGeneratedByPact : messagesGeneratedByPact) {
            // Convert the Pact message to a String (JSON payload)
            String jsonReceived = messageGeneratedByPact.contentsAsString();

            // prepare message properties
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");

            // Create a Spring AMQP Message with the JSON payload and optional headers
            org.springframework.amqp.core.Message messageToBeSentByRabbit = new org.springframework.amqp.core.Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);

            // Simulate receiving the message in the RabbitMQ listener
            assertDoesNotThrow(() -> {
                listener.receiveSuggestionCreated(messageToBeSentByRabbit);
            });

            // somehow optional: verify interactions with the mocked service
            verify(suggestionService, times(1)).createSuggestion(any(SuggestionViewAMQP.class));
        }
    }
}