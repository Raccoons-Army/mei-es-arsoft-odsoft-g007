package pt.psoft.g1.psoftg1.shared.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQPMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseSyncService {

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private LendingViewAMQPMapper lendingMapper;

    @Autowired
    private ReaderDetailsMapper readerDetailsMapper;

    @RabbitListener(queues = RabbitmqClientConfig.LENDING_DB_SYNC_QUEUE)
    public String handleLendingDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<Lending> allData = lendingRepository.findAll();

            // Convert to DTOs using the mapper
            List<LendingViewAMQP> lendingDTOs = lendingMapper.toLendingViewAMQP(allData);

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(lendingDTOs);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }

    @RabbitListener(queues = RabbitmqClientConfig.READER_DB_SYNC_QUEUE)
    public String handleReaderDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<ReaderDetails> allData = readerRepository.findAll();

            List<ReaderDTO> dtoList = readerDetailsMapper.toDtoList(allData);

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(dtoList);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }
}

