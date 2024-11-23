package pt.psoft.g1.psoftg1.bookmanagement.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("jpa")
public class BookServiceAndJpaRepositoryIT {

    @Autowired
    private BookServiceImpl bookService;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @MockBean
    private IdGenerationStrategy<String> idGenerationStrategy;

    @BeforeAll
    public void setup() {
        Genre genreMock = mock(Genre.class);
        when(genreMock.getGenre()).thenReturn("Science Fiction");
        genreRepository.save(genreMock);
    }

    @Test
    public void testCreateBook_Success() {
        // Arrange
        String isbn = "9789720706386";  // Valid ISBN-13 for this test
        CreateBookRequest request = mock(CreateBookRequest.class);

        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getDescription()).thenReturn("Description of test book");
        when(request.getAuthors()).thenReturn(List.of("author123"));
        when(request.getGenre()).thenReturn("Science Fiction");
        when(request.getPhoto()).thenReturn(null);
        when(request.getPhotoURI()).thenReturn("https://example.com/photo.jpg");

        // Act
        Book createdBook = bookService.create(request, isbn);

        // Assert
        assertNotNull(createdBook.getPk());
        assertEquals(isbn, createdBook.getIsbn());
        assertEquals("Test Book Title", createdBook.getTitle());
    }

    @Test
    public void testCreateBook_AlreadyExists() {
        // Arrange
        String isbn = "9789723716160";  // Valid ISBN-13 for this test
        CreateBookRequest bookRequest = new CreateBookRequest();

        bookRequest.setTitle("Test Book");
        bookRequest.setGenre("Science Fiction");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");

        bookService.create(bookRequest, isbn);

        // Act & Assert
        assertThrows(ConflictException.class, () -> {
            bookService.create(bookRequest, isbn);
        });
    }

    @Test
    public void testCreateBook_GenreNotFound() {
        // Arrange
        String isbn = "9789895612864";  // Valid ISBN-13 for this test
        CreateBookRequest bookRequest = new CreateBookRequest();

        bookRequest.setTitle("Test Book");
        bookRequest.setGenre("None");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            bookService.create(bookRequest, isbn);
        });
    }

    @Test
    public void testUpdateBook_Success() {
        // Arrange
        String isbn = "9782722203402";  // Valid ISBN-13 for this test
        CreateBookRequest bookRequest = new CreateBookRequest();

        bookRequest.setTitle("Test Book");
        bookRequest.setGenre("Science Fiction");
        bookRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        bookRequest.setDescription("A description of the test book.");

        // Create a book to update
        Book book = bookService.create(bookRequest, isbn);

        UpdateBookRequest updateRequest = new UpdateBookRequest();
        updateRequest.setIsbn(isbn);
        updateRequest.setTitle("Test Book Updated");
        updateRequest.setGenre("Science Fiction");
        updateRequest.setAuthors(List.of("f9d73c0656b5b3f31e03"));
        updateRequest.setDescription("A description of the test book.");

        // Act
        Book updatedBook = bookService.update(updateRequest, String.valueOf(book.getVersion()));

        // Assert
        assertEquals("Test Book Updated", updatedBook.getTitle());
        assertEquals(book.getPk(), updatedBook.getPk());
    }

    @Test
    public void testFindByGenre_Success() {
        // Arrange
        String isbn = "9789722328296";  // Valid ISBN-13 for this test
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getDescription()).thenReturn("Description of test book");
        when(request.getAuthors()).thenReturn(List.of("author123"));
        when(request.getGenre()).thenReturn("Science Fiction");
        when(request.getPhoto()).thenReturn(null);

        bookService.create(request, isbn);

        // Act
        List<Book> books = bookService.findByGenre("Science Fiction");

        // Assert
        assertFalse(books.isEmpty());
        assertEquals("Science Fiction", books.get(0).getGenre().getGenre());
    }
}
