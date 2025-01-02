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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQP;
import pt.psoft.g1.psoftg1.recommendationmanagement.api.RecommendationViewAMQPMapperImpl;
import pt.psoft.g1.psoftg1.recommendationmanagement.infrastructure.publishers.impl.RecommendationEventsRabbitmqPublisherImpl;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.publishers.RecommendationEventPublisher;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationService;

import java.time.LocalDate;
import java.util.HashMap;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {RecommendationEventsRabbitmqPublisherImpl.class, RecommendationService.class, RecommendationViewAMQPMapperImpl.class}
        , properties = {
        "stubrunner.amqp.mockConnection=true",
        "spring.profiles.active=test"
}
)
@Provider("recommendation_event-producer")
@PactFolder("target/pacts")
public class RecommendationsProducerCDCIT {

        private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationsProducerCDCIT.class);

        @Autowired
        RecommendationEventPublisher recommendationEventPublisher;

        @Autowired
        RecommendationViewAMQPMapperImpl recommendationViewAMQPMapper;

        @MockBean
        RabbitTemplate template;

        @MockBean
        @Qualifier("recommendationsExchange")
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

        @PactVerifyProvider("a recommendation created event")
        public MessageAndMetadata recommendationCreated() throws JsonProcessingException {

            Recommendation recommendation = new Recommendation(null, null, true);
            recommendation.defineBook(new Book("9798669441685"));
            recommendation.defineReaderDetails(new ReaderDetails("2024/10"));
            recommendation.setCreatedAt(LocalDate.now());
            recommendation.setVersion(1L);

            RecommendationViewAMQP recommendationViewAMQP = recommendationEventPublisher.sendRecommendationCreated(recommendation);

            Message<String> message = new RecommendationMessageBuilder().withRecommendation(recommendationViewAMQP).build();

            return generateMessageAndMetadata(message);
        }

        private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
            HashMap<String, Object> metadata = new HashMap<String, Object>();
            message.getHeaders().forEach((k, v) -> metadata.put(k, v));

            return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
        }
}
