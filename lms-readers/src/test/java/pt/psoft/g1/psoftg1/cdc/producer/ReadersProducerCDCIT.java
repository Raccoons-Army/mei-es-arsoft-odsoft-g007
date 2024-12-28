package pt.psoft.g1.psoftg1.cdc.producer;

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
import pt.psoft.g1.psoftg1.publishers.ReaderEventsPublisher;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.publishers.impl.ReaderEventsRabbitMQPublisherImpl;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {ReaderEventsRabbitMQPublisherImpl.class, ReaderService.class}
        , properties = {
        "stubrunner.amqp.mockConnection=true",
        "spring.profiles.active=test"
}
)
@Provider("reader_event-producer")

@PactFolder("target/pacts")
public class ReadersProducerCDCIT {

        private static final Logger LOGGER = LoggerFactory.getLogger(ReadersProducerCDCIT.class);

        @Autowired
        ReaderEventsPublisher readerEventsPublisher;

        @MockBean
        RabbitTemplate template;

        @MockBean
        @Qualifier("readersExchange")
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

        @PactVerifyProvider("a reader created event")
        public MessageAndMetadata readerCreated() throws JsonProcessingException {
            List<String> interestList = new ArrayList<>();
            interestList.add("Fantasia");
            interestList.add("Infantil");

            ReaderDetails readerDetails = new ReaderDetails("2024/10",
                    "17-12-2001", "915367890", true, true,
                    true, "photo", "pedro@gmail.com", interestList);

            ReaderViewAMQP readerViewAMQP = readerEventsPublisher.sendReaderCreated(readerDetails);

            Message<String> message = new ReadersMessageBuilder().withReader(readerViewAMQP).build();

            return generateMessageAndMetadata(message);
        }

        private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
            HashMap<String, Object> metadata = new HashMap<String, Object>();
            message.getHeaders().forEach((k, v) -> metadata.put(k, v));

            return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
        }
}
