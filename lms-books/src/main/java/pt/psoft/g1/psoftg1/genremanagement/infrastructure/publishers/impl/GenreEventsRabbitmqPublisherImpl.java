package pt.psoft.g1.psoftg1.genremanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQPMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.publishers.GenreEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.GenreEvents;

@Service
@RequiredArgsConstructor
public class GenreEventsRabbitmqPublisherImpl implements GenreEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    @Qualifier("genresExchange")
    private DirectExchange direct;
    private final GenreViewAMQPMapper genreViewAMQPMapper;

    @Override
    public void sendGenreCreated(Genre genre) {
        sendGenreEvent(genre, GenreEvents.GENRE_CREATED);
    }

    public void sendGenreEvent(Genre genre, String genreEventType) {

        try {
                ObjectMapper objectMapper = new ObjectMapper();

            GenreViewAMQP genreViewAMQP = genreViewAMQPMapper.toGenreViewAMQP(genre);

            String jsonString = objectMapper.writeValueAsString(genreViewAMQP);

            this.template.convertAndSend(direct.getName(), genreEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending genre event: '" + ex.getMessage() + "'");
        }
    }
}