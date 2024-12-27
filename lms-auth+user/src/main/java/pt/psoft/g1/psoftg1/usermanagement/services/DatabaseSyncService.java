package pt.psoft.g1.psoftg1.usermanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.configuration.RabbitMQClientConfig;
import pt.psoft.g1.psoftg1.usermanagement.mapper.UserMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseSyncService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @RabbitListener(queues = RabbitMQClientConfig.DB_SYNC_QUEUE)
    public String handleDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<User> allData = userRepository.findAll();

            List<UserDTO> userDTOS = userMapper.toDtoList(allData);

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(userDTOS);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }
}

