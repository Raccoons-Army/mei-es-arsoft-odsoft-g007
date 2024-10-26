package pt.psoft.g1.psoftg1.lendingmanagement.services;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.LendingForbiddenException;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@Transactional
@SpringBootTest
class LendingServiceImplIT {
    @Autowired
    private LendingService lendingService;
    @Autowired
    private LendingRepository lendingRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    private Lending lending;
    private ReaderDetails readerDetails;
    private Reader reader;
    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() throws InstantiationException {
        FactoryGenre doubleFactoryGenre = mock(FactoryGenre.class);
        FactoryAuthor doubleFactoryAuthor = mock(FactoryAuthor.class);
        FactoryUser factoryUserDouble = mock(FactoryUser.class);

        author = new Author("aa1","Manuel Antonio Pina",
                "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões",
                null);
        authorRepository.save(author);

        genre = new Genre("Género");
        genreRepository.save(genre);

        List<Author> authors = List.of(author);
        book = new Book("9782826012092",
                "O Inspetor Max",
                "conhecido pastor-alemão que trabalha para a Judiciária, vai ser fundamental para resolver um importante caso de uma rede de malfeitores que quer colocar uma bomba num megaconcerto de uma ilustre cantora",
                null, doubleFactoryGenre, doubleFactoryAuthor);

        book.defineGenre(genre.getGenre());
        book.addAuthor("aa1", "Manuel Antonio Pina",
                "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões",
                null);

        bookRepository.save(book);

        reader = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
        userRepository.save(reader);

        readerDetails = new ReaderDetails(1,
                "2000-01-01",
                "919191919",
                true,
                true,
                true,
                null,factoryUserDouble, doubleFactoryGenre);

        readerDetails.defineReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");

        readerRepository.save(readerDetails);

        // Create and save the lending
        lending = Lending.newBootstrappingLending(
                "aa1",
                book,
                readerDetails,
                LocalDate.now().getYear(),
                999,
                LocalDate.of(LocalDate.now().getYear(), 1,1),
                LocalDate.of(LocalDate.now().getYear(), 1,11),
                15,
                300);
        lendingRepository.save(lending);

    }

    @AfterEach
    void tearDown() {
        lendingRepository.delete(lending);
        readerRepository.delete(readerDetails);
        userRepository.delete(reader);
        bookRepository.delete(book);
        genreRepository.delete(genre);
        authorRepository.delete(author);
    }

    @Test
    void testFindByLendingNumber() {
        assertThat(lendingService.findByLendingNumber(LocalDate.now().getYear() + "/999")).isPresent();
        assertThat(lendingService.findByLendingNumber(LocalDate.now().getYear() + "/1")).isEmpty();
    }
/*
    @Test
    void testListByReaderNumberAndIsbn() {

    }
 */
    @Test
    void testCreate() {
        var request = new CreateLendingRequest("9782826012092",
                LocalDate.now().getYear() + "/1");
        var lending1 = lendingService.create(request);
        assertThat(lending1).isNotNull();
        var lending2 = lendingService.create(request);
        assertThat(lending2).isNotNull();
        var lending3 = lendingService.create(request);
        assertThat(lending3).isNotNull();

        // 4th lending
        assertThrows(LendingForbiddenException.class, () -> lendingService.create(request));

        lendingRepository.delete(lending3);
        lendingRepository.save(Lending.newBootstrappingLending(
                "aa1",
                book,
                readerDetails,
                2024,
                997,
                LocalDate.of(2024, 3,1),
                null,
                15,
                300));

        // Having an overdue lending
        assertThrows(LendingForbiddenException.class, () -> lendingService.create(request));

    }

    @Test
    void testSetReturned() {
        int year = 2024, seq = 888;
        var notReturnedLending = lendingRepository.save(Lending.newBootstrappingLending(
                "aa1",
                book,
                readerDetails,
                year,
                seq,
                LocalDate.of(2024, 3,1),
                null,
                15,
                300));
        var request = new SetLendingReturnedRequest(null);
        assertThrows(StaleObjectStateException.class,
                () -> lendingService.setReturned(year + "/" + seq, request, (notReturnedLending.getVersion()-1)));

        assertDoesNotThrow(
                () -> lendingService.setReturned(year + "/" + seq, request, notReturnedLending.getVersion()));
    }
/*
    @Test
    void testGetAverageDuration() {
    }

    @Test
    void testGetOverdue() {
    }

 */
}
