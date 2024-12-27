package pt.psoft.g1.psoftg1.readermanagement.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.configuration.RabbitMQClientConfig;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseSyncClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReaderViewAMQPMapper readerViewAMQPMapper;

    @Transactional
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
                List<ReaderViewAMQP> readersAMQPList = objectMapper.readValue(
                        response,
                        new TypeReference<>() {}
                );

                List<ReaderDetails> readersList = readerViewAMQPMapper.toReaderDetails(readersAMQPList);

                // Save data to the new instance's H2 database
                for (ReaderDetails readerDetails : readersList) {
                    Optional<ReaderDetails> readerExists = readerRepository.findByReaderNumber(readerDetails.getReaderNumber());
                    Optional<User> userExists = userRepository.findByUsername(readerDetails.getUsername());

                    List<String> readersGenres = readerDetails.getInterestList();

                    for(String genre : readersGenres) {
                        Optional<Genre> genreExists = genreRepository.findByString(genre);
                        if(genreExists.isEmpty()) {
                            Genre saveGenre = new Genre(genre);
                            genreRepository.save(saveGenre);
                        }
                    }

                    if(userExists.isEmpty()) {
                        User user = new User(readerDetails.getUsername());
                        userRepository.save(user);
                    }

                    if(readerExists.isEmpty()) {
                        readerRepository.save(readerDetails);
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

