package pt.psoft.g1.psoftg1.genremanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GenreEventRabbitmqReceiver {

    private final GenreService genreService;

    @RabbitListener(queues = "#{autoDeleteQueue_Genre_Created.name}")
    public void receiveGenreCreated(Message msg) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            GenreViewAMQP genreViewAMQP = objectMapper.readValue(jsonReceived, GenreViewAMQP.class);

            System.out.println(" [x] Received Genre Created by AMQP: " + msg + ".");
            try {
                genreService.create(genreViewAMQP);
                System.out.println(" [x] New genre inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Genre already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving genre event from AMQP: '" + ex.getMessage() + "'");
        }
    }
}