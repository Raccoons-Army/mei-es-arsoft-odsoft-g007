package pt.psoft.g1.psoftg1.bookmanagement.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseSyncClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    public void syncDatabase() {
        try {
            // Send RPC request to the queue
            String response = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.BOOK_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseAuthor = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.AUTHOR_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseGenre = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.GENRE_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseSuggested = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.SUGGESTED_DB_SYNC_QUEUE, "SYNC_REQUEST");

            // Handle the case where no other instance responds
            if (response == null) {
                System.out.println("No existing instances found. Starting with an empty database.");
                return; // Skip synchronization for the first instance
            }

            if (!response.contains("ERROR")) {
                // Deserialize the received data
                ObjectMapper objectMapper = new ObjectMapper();

                // Deserialize the response into a list of SuggestionViewAMQP DTOs
                List<BookViewAMQP> dtoList = objectMapper.readValue(
                        response,
                        new TypeReference<>() {}
                );

                List<Author> authors = objectMapper.readValue(
                        responseAuthor,
                        new TypeReference<>() {}
                );

                List<Genre> genres = objectMapper.readValue(
                        responseGenre,
                        new TypeReference<>() {}
                );

                List<SuggestedBook> suggestedBooks = objectMapper.readValue(
                        responseSuggested,
                        new TypeReference<>() {}
                );

                for(Author author : authors) {
                    Optional<Author> authorExists = authorRepository.findByAuthorNumber(author.getAuthorNumber());
                    if(authorExists.isEmpty()) {
                        Author newAuthor;
                        if(author.getPhoto() == null) {
                            newAuthor = new Author(author.getAuthorNumber(), author.getName(), author.getBio(), null);
                        }else{
                            newAuthor = new Author(author.getAuthorNumber(), author.getName(), author.getBio(), author.getPhoto().getPhotoFile());
                        }
                        authorRepository.save(newAuthor);
                    }
                }

                for(Genre genre : genres) {
                    Optional<Genre> genreExists = genreRepository.findByString(genre.getGenre());
                    if(genreExists.isEmpty()) {
                        Genre newGenre = new Genre(genre.getGenre());
                        genreRepository.save(newGenre);
                    }
                }

                for(SuggestedBook suggestedBook : suggestedBooks) {
                    Optional<SuggestedBook> suggestedBookExists = suggestedBookRepository.findById(suggestedBook.getPk());
                    if(suggestedBookExists.isEmpty()) {
                        SuggestedBook newSuggestedBook = new SuggestedBook(suggestedBook.getIsbn().toString());
                        suggestedBookRepository.save(newSuggestedBook);
                    }
                }

                // Map the DTOs to Suggestion entities
                List<Book> books = bookMapper.toBook(dtoList);

                // Save data to the new instance's H2 database
                for (Book book : books) {
                    Optional<Book> bookExists = bookRepository.findByIsbn(book.getIsbn());
                    if(bookExists.isEmpty()) {
                        List<Author> bookAuthors = new ArrayList<>();
                        for (Author author : book.getAuthors()) {
                            Optional<Author> authorExists = authorRepository.findByAuthorNumber(author.getAuthorNumber());
                            authorExists.ifPresent(bookAuthors::add);
                        }
                        Optional<Genre> genreExists = genreRepository.findByString(book.getGenre().getGenre());
                        genreExists.ifPresent(book::setGenre);
                        book.setAuthors(bookAuthors);
                        bookRepository.save(book);
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

