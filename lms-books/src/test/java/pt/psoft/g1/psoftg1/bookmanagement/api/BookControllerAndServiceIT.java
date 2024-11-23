package pt.psoft.g1.psoftg1.bookmanagement.api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.TestSecurityConfig;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "pt.psoft.g1.psoftg1.bookmanagement")
@Import(TestSecurityConfig.class)
public class BookControllerAndServiceIT {

    @Autowired
    private MockMvc mockMvc;  // Mock MVC to simulate HTTP requests

    @Autowired
    private BookController bookController;  // Controller to be tested

    @Autowired
    private BookService bookService;  // Service to be tested

    @MockBean
    private BookRepository bookRepository;  // Mock the BookRepository

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private PhotoRepository photoRepository;

    @MockBean
    private ForbiddenNameRepository forbiddenNameRepository;

    @MockBean
    private ConcurrencyService concurrencyService;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private BookViewMapper bookViewMapper;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private IdGenerationStrategy<String> idGenerationStrategy;

    private final String isbn = "9780471486480";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateBook() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        CreateBookRequest bookRequest = new CreateBookRequest();

        bookRequest.setTitle("Test Book");
        bookRequest.setGenre("Infantil");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");


        Genre mockGenre = mock(Genre.class);
        Author mockAuthor = mock(Author.class);

        // Mock repositories
        when(genreRepository.findByString("Infantil")).thenReturn(Optional.of(mockGenre));  // Mock GenreRepository
        when(authorRepository.findByAuthorNumber("f9d73c0656b5b3f31e03")).thenReturn(Optional.of(mockAuthor)); // Mock AuthorRepository

        Book mockBook = new Book("9780471486480", "Test Book", "A description of the test book.", null, factoryGenreDouble, factoryAuthorDouble);
        mockBook.setVersion(1L);
        when(fileStorageService.getRequestPhoto(mockFile)).thenReturn("photo.jpg");
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);

        ResponseEntity<BookView> response = bookController.create(bookRequest, isbn);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(bookRepository, times(1)).save(any(Book.class)); // Ensure that the repository save method was called
    }

    @Test
    void testCreateBook_IsbnExistent() throws Exception {

        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        CreateBookRequest bookRequest = new CreateBookRequest();
        bookRequest.setTitle("Test Book");
        bookRequest.setGenre("Infantil");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");

        Book bookMock = new Book("9780471486480", "Test Book", "A description of the test book.", null, factoryGenreDouble, factoryAuthorDouble);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock));

        ResponseEntity<BookView> response = bookController.create(bookRequest, isbn);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testCreateBook_BadRequest() throws Exception {
        CreateBookRequest bookRequest = new CreateBookRequest();
        bookRequest.setTitle(null);
        bookRequest.setGenre("Infantil");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");

        ResponseEntity<BookView> response = bookController.create(bookRequest, isbn);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateBook_GenreNotFound() throws Exception {
        String genre = "Test Genre";

        CreateBookRequest bookRequest = new CreateBookRequest();
        bookRequest.setTitle(null);
        bookRequest.setGenre("Infantil");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(genre)).thenReturn(Optional.empty());

        ResponseEntity<BookView> response = bookController.create(bookRequest, isbn);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testGetBookByIsbn() throws Exception {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        Book book = new Book("9780471486480", "Test Book", "A description of the test book.", null, factoryGenreDouble, factoryAuthorDouble);
        book.setVersion(1L);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        ResponseEntity<BookView> response = bookController.findByIsbn(isbn);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetBookByIsbn_NotFound() throws Exception {
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{isbn}", isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookPhoto() throws Exception {

        Book bookMock = mock(Book.class);
        // Set the photo URI of the book
        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getPhoto()).thenReturn(mock(Photo.class));
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock));

        mockMvc.perform(delete("/api/books/{isbn}/photo", bookMock.getIsbn()))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBookPhoto_NotFound() throws Exception {
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/books/{isbn}/photo", isbn))
                .andExpect(status().isNotFound());
    }
}
