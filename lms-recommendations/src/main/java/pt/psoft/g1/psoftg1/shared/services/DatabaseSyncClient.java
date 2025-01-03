package pt.psoft.g1.psoftg1.shared.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.recommendationmanagement.repositories.RecommendationRepository;
import pt.psoft.g1.psoftg1.recommendationmanagement.services.RecommendationDTO;
import pt.psoft.g1.psoftg1.shared.utils.DateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseSyncClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private FactoryBook _factoryBook;

    @Autowired
    private FactoryReaderDetails _factoryReaderDetails;

    public void syncDatabase() {
        try {
            // Send RPC request to the queue
            String response = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.RECOMMENDATION_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseBook = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.BOOK_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseReader = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.READER_DB_SYNC_QUEUE, "SYNC_REQUEST");

            // Handle the case where no other instance responds
            if (response == null) {
                System.out.println("No existing instances found. Starting with an empty database.");
                return; // Skip synchronization for the first instance
            }

            if (!response.contains("ERROR")) {
                // Deserialize the received data
                ObjectMapper objectMapper = new ObjectMapper();

                List<Book> books = objectMapper.readValue(responseBook, new TypeReference<>() {});
                List<ReaderDetails> readers = objectMapper.readValue(responseReader, new TypeReference<>() {});
                List<RecommendationDTO> recommendationDTOS = objectMapper.readValue(response, new TypeReference<>() {});

                for(Book book : books) {
                    Optional<Book> bookExists = bookRepository.findByIsbn(book.getIsbn());
                    if(bookExists.isEmpty()) {
                        bookRepository.save(book);
                    }
                }

                for(ReaderDetails readerDetails : readers) {
                    Optional<ReaderDetails> readerExists = readerRepository.findByReaderNumber(readerDetails.getReaderNumber());
                    if(readerExists.isEmpty()) {
                        readerRepository.save(readerDetails);
                    }
                }

                for(RecommendationDTO recommendationDTO : recommendationDTOS) {
                    LocalDate createdAt = parseLocalDate(recommendationDTO.getCreatedAt());
                    Recommendation newRecommendation = new Recommendation(_factoryBook, _factoryReaderDetails, recommendationDTO.isPositive());
                    recommendationRepository.save(newRecommendation);
                }

                System.out.println("Database synchronized successfully!");
            } else {
                System.err.println("Failed to synchronize database. Response: " + response);
            }
        } catch (Exception e) {
            System.err.println("Error during database synchronization: " + e.getMessage());
        }
    }

    protected LocalDate parseLocalDate(String date) {
        return date != null ? LocalDate.parse(date, DateUtils.ISO_DATE_FORMATTER) : null;
    }
}

