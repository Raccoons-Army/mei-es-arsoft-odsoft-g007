package pt.psoft.g1.psoftg1.bookmanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.repositories.SuggestedBookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseSyncService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private SuggestedBookRepository suggestedBookRepository;

    @Autowired
    private BookViewAMQPMapper bookMapper;

    @RabbitListener(queues = RabbitmqClientConfig.BOOK_DB_SYNC_QUEUE)
    public String handleDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<Book> allData = bookRepository.findAll();

            // Convert to DTOs using the mapper
            List<BookViewAMQP> bookDTOs = bookMapper.toBookViewAMQP(allData);

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(bookDTOs);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }

    @RabbitListener(queues = RabbitmqClientConfig.AUTHOR_DB_SYNC_QUEUE)
    public String handleAuthorDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<Author> allData = authorRepository.findAll();

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(allData);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }

    @RabbitListener(queues = RabbitmqClientConfig.GENRE_DB_SYNC_QUEUE)
    public String handleGenreDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<Genre> allData = genreRepository.findAll();

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(allData);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }

    @RabbitListener(queues = RabbitmqClientConfig.SUGGESTED_DB_SYNC_QUEUE)
    public String handleSuggestedDatabaseSyncRequest(String request) {
        try {
            // Fetch all data from the database
            List<SuggestedBook> allData = suggestedBookRepository.findAll();

            // Convert the DTOs to JSON for transfer
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(allData);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }
}

