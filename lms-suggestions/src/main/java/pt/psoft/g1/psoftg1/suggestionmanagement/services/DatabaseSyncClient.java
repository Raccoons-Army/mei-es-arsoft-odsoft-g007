package pt.psoft.g1.psoftg1.suggestionmanagement.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQPMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseSyncClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private SuggestionViewAMQPMapper suggestionMapper;
    @Autowired
    private ReaderViewAMQPMapper readerViewAMQPMapper;

    public void syncDatabase() {
        try {
            // Send RPC request to the queue
            String response = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.DB_SYNC_QUEUE, "SYNC_REQUEST");
            String responseReader = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.READER_DB_SYNC_QUEUE, "SYNC_REQUEST");

            // Handle the case where no other instance responds
            if (response == null) {
                System.out.println("No existing instances found. Starting with an empty database.");
                return; // Skip synchronization for the first instance
            }

            if (!response.contains("ERROR")) {
                // Deserialize the received data
                ObjectMapper objectMapper = new ObjectMapper();

                // Deserialize the response into a list of SuggestionViewAMQP DTOs
                List<SuggestionViewAMQP> dtoList = objectMapper.readValue(
                        response,
                        new TypeReference<>() {}
                );
                List<ReaderDetailsViewAMQP> readersAmqp = objectMapper.readValue(responseReader, new TypeReference<>() {
                });

                List<ReaderDetails> readers = readerViewAMQPMapper.toReaderDetails(readersAmqp);
                for (ReaderDetails readerDetails : readers) {
                    Optional<ReaderDetails> readerExists = readerRepository.findByReaderNumber(readerDetails.getReaderNumber());
                    if (readerExists.isEmpty()) {
                        readerRepository.save(readerDetails);
                    }
                }

                // Map the DTOs to Suggestion entities
                List<Suggestion> suggestions = suggestionMapper.toSuggestionList(dtoList);

                // Save data to the new instance's H2 database
                for (Suggestion suggestion : suggestions) {
                    Optional<ReaderDetails> readerDetails = readerRepository.findByReaderNumber(suggestion.getReaderDetails().getReaderNumber());
                    if(readerDetails.isPresent()) {
                        suggestion.setReaderDetails(readerDetails.get());
                    }else{
                        ReaderDetails newReader = new ReaderDetails(suggestion.getReaderDetails().getReaderNumber());
                        readerRepository.save(newReader);
                    }
                    suggestionRepository.save(suggestion);
                }

                System.out.println("Database synchronized successfully!");
            } else {
                System.err.println("Failed to synchronize database. Response: " + response);
            }
        } catch (Exception e) {
            System.err.println("Error during database synchronization: " + e.getMessage());
        }
    }
}

