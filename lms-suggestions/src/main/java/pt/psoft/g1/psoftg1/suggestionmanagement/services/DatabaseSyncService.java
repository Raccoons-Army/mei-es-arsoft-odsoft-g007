package pt.psoft.g1.psoftg1.suggestionmanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQPMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseSyncService {

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private SuggestionViewAMQPMapper suggestionMapper;

    @RabbitListener(queues = RabbitmqClientConfig.DB_SYNC_QUEUE)
    public String handleDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<Suggestion> allData = suggestionRepository.findAll();

            // Convert to DTOs using the mapper
            List<SuggestionViewAMQP> suggestionDTOs = suggestionMapper.toSuggestionViewAMQP(allData);

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(suggestionDTOs);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }
}

