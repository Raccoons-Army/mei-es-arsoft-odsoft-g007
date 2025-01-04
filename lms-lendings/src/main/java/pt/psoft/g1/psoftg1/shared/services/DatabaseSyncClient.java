package pt.psoft.g1.psoftg1.shared.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.configuration.RabbitmqClientConfig;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderDTO;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.util.DateUtils;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DatabaseSyncClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private IdGenerationStrategy<String> idGenerationStrategy;

    @Autowired
    private ReaderViewAMQPMapper readerViewAMQPMapper;
    @Autowired
    private BookViewAMQPMapper bookViewAMQPMapper;

    public void syncDatabase() {
        try {
            // Send RPC request to the queue
            String response = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.LENDING_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseReaders = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.READER_DB_SYNC_QUEUE, "SYNC_REQUEST");

            String responseBooks = (String) rabbitTemplate.convertSendAndReceive(RabbitmqClientConfig.BOOK_DB_SYNC_QUEUE, "SYNC_REQUEST");

            // Handle the case where no other instance responds
            if (response == null) {
                System.out.println("No existing instances found. Starting with an empty database.");
                return; // Skip synchronization for the first instance
            }

            if (!response.contains("ERROR")) {
                // Deserialize the received data
                ObjectMapper objectMapper = new ObjectMapper();

                // Deserialize the response into a list of SuggestionViewAMQP DTOs
                List<LendingViewAMQP> dtoList = objectMapper.readValue(
                        response,
                        new TypeReference<>() {}
                );

                List<ReaderViewAMQP> readersAmqp = objectMapper.readValue(
                        responseReaders,
                        new TypeReference<>() {}
                );

                List<BookViewAMQP> booksAmqp = objectMapper.readValue(
                        responseBooks,
                        new TypeReference<>() {}
                );

                List<ReaderDetails> readersList = readerViewAMQPMapper.toReaderDetails(readersAmqp);
                for(ReaderDetails reader : readersList) {
                    Optional<ReaderDetails> readerDetailsExists = readerRepository.findByReaderNumber(reader.getReaderNumber());
                    if(readerDetailsExists.isEmpty()) {
                        readerRepository.save(reader);
                    }
                }

                List<Book> booksList = bookViewAMQPMapper.toBook(booksAmqp);
                for(Book book : booksList) {
                    Optional<Book> bookExists = bookRepository.findByIsbn(book.getIsbn());
                    if(bookExists.isEmpty()) {
                        bookRepository.save(book);
                    }
                }

                // Save data to the new instance's H2 database
                for (LendingViewAMQP lendingViewAMQP : dtoList) {
                    Optional<Lending> lendingExists = lendingRepository.findByLendingNumber(lendingViewAMQP.getLendingNumber());

                    if(lendingExists.isEmpty()) {
                        Optional<Book> bookExists = bookRepository.findByIsbn(lendingViewAMQP.getBookIsbn());
                        Optional<ReaderDetails> readerDetailsExists = readerRepository.findByReaderNumber(lendingViewAMQP.getReaderNumber());

                        Book book;
                        if(bookExists.isEmpty()) {
                            book = new Book(lendingViewAMQP.getBookIsbn());
                            bookRepository.save(book);
                        }else{
                            book = bookExists.get();
                        }

                        ReaderDetails readerDetails = null;
                        if(readerDetailsExists.isPresent()) {
                            readerDetails = readerDetailsExists.get();
                        }

                        LocalDate startDate = parseLocalDate(lendingViewAMQP.getStartDate());
                        LocalDate limitDate = parseLocalDate(lendingViewAMQP.getLimitDate());
                        Lending lending =  new Lending(idGenerationStrategy.generateId(), book, readerDetails, lendingViewAMQP.getLendingNumber(),
                                startDate, limitDate, lendingViewAMQP.getFineValuePerDayInCents());

                        lendingRepository.save(lending);
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

    protected LocalDate parseLocalDate(String date) {
        return date != null ? LocalDate.parse(date, DateUtils.ISO_DATE_FORMATTER) : null;
    }
}

