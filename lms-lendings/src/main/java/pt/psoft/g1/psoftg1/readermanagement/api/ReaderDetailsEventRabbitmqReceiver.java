package pt.psoft.g1.psoftg1.readermanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ReaderDetailsEventRabbitmqReceiver {

    private final ReaderService readerService;

    @RabbitListener(queues = "#{autoDeleteQueue_ReaderDetails_Created.name}")
    public void receiveReaderDetailsCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            ReaderDetailsViewAMQP readerDetailsViewAMQP = objectMapper.readValue(jsonReceived, ReaderDetailsViewAMQP.class);

            System.out.println(" [x] Received ReaderDetails Created by AMQP: " + msg + ".");
            try {
                readerService.create(readerDetailsViewAMQP);
                System.out.println(" [x] New readerDetails inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] ReaderDetails already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving readerDetails event from AMQP: '" + ex.getMessage() + "'");
        }
    }

    @RabbitListener(queues = "#{autoDeleteQueue_ReaderDetails_Deleted.name}")
    public void receiveReaderDetailsDeleted(String in) {
        System.out.println(" [x] Received ReaderDetails Deleted '" + in + "'");
    }
}
