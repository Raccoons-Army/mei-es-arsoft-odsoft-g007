package pt.psoft.g1.psoftg1.shared.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQPMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQP;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreViewAMQPMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

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
    private BookViewAMQPMapper bookMapper;
    @Autowired
    private AuthorViewAMQPMapper authorViewAMQPMapper;
    @Autowired
    private GenreViewAMQPMapper genreViewAMQPMapper;

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
            List<AuthorViewAMQP> authorDTOs = authorViewAMQPMapper.toAuthorViewAMQP(allData);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(authorDTOs);

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
            List<GenreViewAMQP> genreDTOs = genreViewAMQPMapper.toGenreViewAMQP(allData);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(genreDTOs);

        } catch (Exception e) {
            return "ERROR: Unable to process sync request. Cause: " + e.getMessage();
        }
    }
}

