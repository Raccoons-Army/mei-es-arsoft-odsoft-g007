package pt.psoft.g1.psoftg1.readermanagement.infraestructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.publishers.ReaderEventsPublisher;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;

@Service
@RequiredArgsConstructor
public class ReaderEventsRabbitMQPublisherImpl implements ReaderEventsPublisher {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    private final ReaderViewAMQPMapper readerViewAMQPMapper;

    @Override
    public void sendReaderCreated(ReaderDetails readerDetails) {
        sendReaderEvent(readerDetails, readerDetails.getVersion(), ReaderEvents.READER_CREATED);
    }

    @Override
    public void sendReaderUpdated(ReaderDetails readerDetails, Long currentVersion) {
        sendReaderEvent(readerDetails, readerDetails.getVersion(), ReaderEvents.READER_UPDATED);
    }

    @Override
    public void sendReaderDeleted(ReaderDetails readerDetails, Long currentVersion) {
        sendReaderEvent(readerDetails, readerDetails.getVersion(), ReaderEvents.READER_DELETED);
    }

    public void sendReaderEvent(ReaderDetails readerDetails, Long currentVersion, String userEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ReaderViewAMQP readerViewAMQP = readerViewAMQPMapper.toReaderViewAMQP(readerDetails);
            readerViewAMQP.setVersion(currentVersion);

            String jsonString = objectMapper.writeValueAsString(readerViewAMQP);

            this.template.convertAndSend(direct.getName(), userEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending reader event: '" + ex.getMessage() + "'");
        }
    }

}
