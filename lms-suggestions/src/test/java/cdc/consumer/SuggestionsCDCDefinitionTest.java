package cdc.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {SuggestionEventRabbitMQReceiver.class, SuggestionService.class}
)
@PactConsumerTest
@PactTestFor(providerName = "suggestion_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class SuggestionsCDCDefinitionTest {

    @MockBean
    SuggestionService suggestionService;

    @Autowired
    SuggestionEventRabbitMQReceiver listener;

    @Pact(consumer = "suggestion_created-consumer")
    V4Pact createSuggestionCreatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("isbn", "6475803429671");
        body.stringType("readerNumber", "2024/10");
        body.stringType("createdAt", "17-12-2024");
        body.stringMatcher("version", "[0-9]+", "1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        V4Pact pact = builder.expectsToReceive("a suggestion created event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();

        // Log the pact for debugging
        System.out.println("Generated Pact: " + pact);

        return pact;
    }

    //
// The following tests are now defined as IT tests, so that the definition of contract and the tests are decoupled.
// Yet, while the body of the tests can be elsewhere, the method signature must be defined here so the contract is generated.
//
    @Test
    @PactTestFor(pactMethod = "createSuggestionCreatedPact")
    void testSuggestionCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
//
//  // Convert the Pact message to a String (JSON payload)
//    String jsonReceived = messages.get(0).contentsAsString();
//
//    // Create a Spring AMQP Message with the JSON payload and optional headers
//    MessageProperties messageProperties = new MessageProperties();
//    messageProperties.setContentType("application/json");
//    Message message = new Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);
//
//    // Simulate receiving the message in the listener
//    assertDoesNotThrow(() -> {
//      listener.receiveBookCreatedMsg(message);
//    });
//
//    // Verify interactions with the mocked service
//    verify(bookService, times(1)).create(any(BookViewAMQP.class));
    }
}
