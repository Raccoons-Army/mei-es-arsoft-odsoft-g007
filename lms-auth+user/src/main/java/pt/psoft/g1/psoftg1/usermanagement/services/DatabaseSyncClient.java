package pt.psoft.g1.psoftg1.usermanagement.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.configuration.RabbitMQClientConfig;
import pt.psoft.g1.psoftg1.usermanagement.mapper.UserMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseSyncClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public void syncDatabase() {
        try {
            // Send RPC request to the queue
            String response = (String) rabbitTemplate.convertSendAndReceive(RabbitMQClientConfig.DB_SYNC_QUEUE, "SYNC_REQUEST");

            // Handle the case where no other instance responds
            if (response == null) {
                System.out.println("No existing instances found. Starting with an empty database.");
                return; // Skip synchronization for the first instance
            }

            if (!response.contains("ERROR")) {
                // Deserialize the received data
                ObjectMapper objectMapper = new ObjectMapper();

                // Deserialize the response into a list of Users
                List<UserDTO> usersList = objectMapper.readValue(
                        response,
                        new TypeReference<>() {}
                );

                // Save data to the new instance's H2 database
                for (UserDTO userDTO : usersList) {
                    Optional<User> userExists = userRepository.findByUsername(userDTO.getUsername());
                    if(userExists.isEmpty()) {
                        User user = userMapper.toEntity(userDTO);
                        userRepository.save(user);
                    }
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

