package cdc.producer;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQPMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQPMapperImpl;
import pt.psoft.g1.psoftg1.suggestionmanagement.infraestructure.publishers.impl.SuggestionEventsRabbitMQPublisherImpl;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.publishers.SuggestionEventPublisher;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {SuggestionEventsRabbitMQPublisherImpl.class, SuggestionService.class, SuggestionViewAMQPMapperImpl.class}
        , properties = {
        "stubrunner.amqp.mockConnection=true",
        "spring.profiles.active=test"
}
)
@Provider("suggestion_event-producer")
@PactFolder("target/pacts")
public class SuggestionsProducerCDCIT {

        private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionsProducerCDCIT.class);

        @Autowired
        SuggestionEventPublisher suggestionEventPublisher;

        @Autowired
        SuggestionViewAMQPMapperImpl suggestionViewAMQPMapper;

        @MockBean
        RabbitTemplate template;

        @MockBean
        @Qualifier("suggestionsExchange")
        DirectExchange direct;

        @TestTemplate
        @ExtendWith(PactVerificationInvocationContextProvider.class)
        void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
            context.verifyInteraction();
        }

        @BeforeEach
        void before(PactVerificationContext context) {
            context.setTarget(new MessageTestTarget());
        }

        @PactVerifyProvider("a suggestion created event")
        public MessageAndMetadata suggestionCreated() throws JsonProcessingException {

            ReaderDetails readerDetails = new ReaderDetails("2024/10");

            Suggestion suggestion = new Suggestion(null, "6475803429671", LocalDate.now(), readerDetails);

            SuggestionViewAMQP suggestionViewAMQP = suggestionEventPublisher.sendSuggestionCreated(suggestion);

            Message<String> message = new SuggestionMessageBuilder().withSuggestion(suggestionViewAMQP).build();

            return generateMessageAndMetadata(message);
        }

        private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
            HashMap<String, Object> metadata = new HashMap<String, Object>();
            message.getHeaders().forEach((k, v) -> metadata.put(k, v));

            return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
        }
}
