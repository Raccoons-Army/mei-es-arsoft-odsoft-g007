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
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.publishers.impl.BookEventsRabbitmqPublisherImpl;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
        ,classes = {BookEventsRabbitmqPublisherImpl.class, BookService.class}
        , properties = {
        "stubrunner.amqp.mockConnection=true",
        "spring.profiles.active=test"
}
)
@Provider("book_event-producer")
@PactFolder("target/pacts")
public class BooksProducerCDCIT {

        private static final Logger LOGGER = LoggerFactory.getLogger(BooksProducerCDCIT.class);

        @Autowired
        BookEventsPublisher bookEventsPublisher;

        @MockBean
        RabbitTemplate template;

        @MockBean
        @Qualifier("booksExchange")
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

        @PactVerifyProvider("a book created event")
        public MessageAndMetadata bookCreated() throws JsonProcessingException {
            List<Author> authorList = new ArrayList<>();
            Genre genre = new Genre("Manga");

            Author author = new Author("1000", "José Rizzler", "José, The Rizzler of the Rizzlers", "photoURI");
            authorList.add(author);

            Book book = new Book("9789896615246", "The Lord of the Rings: The Two Towers",
            "You Shall Not Pass!!!", "photoURI", genre, authorList);

            BookViewAMQP bookViewAMQP = bookEventsPublisher.sendBookCreated(book);

            Message<String> message = new BookMessageBuilder().withBook(bookViewAMQP).build();

            return generateMessageAndMetadata(message);
        }

        private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
            HashMap<String, Object> metadata = new HashMap<String, Object>();
            message.getHeaders().forEach((k, v) -> metadata.put(k, v));

            return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
        }
}
