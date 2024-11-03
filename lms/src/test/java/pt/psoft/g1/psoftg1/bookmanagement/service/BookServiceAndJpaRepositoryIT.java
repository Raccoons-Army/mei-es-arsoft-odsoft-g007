package pt.psoft.g1.psoftg1.bookmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("jpa")
public class BookServiceAndJpaRepositoryIT {

    @Autowired
    private BookServiceImpl bookService;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        // Create a genre
        Genre genreMock = mock(Genre.class);
        when(genreMock.getGenre()).thenReturn("Science Fiction");
        genreRepository.save(genreMock);

        // create an author author123
        Author authorMock = mock(Author.class);
        when(authorMock.getAuthorNumber()).thenReturn("12345");
        when(authorMock.getName()).thenReturn("author123");
        when(authorMock.getBio()).thenReturn("01/01/2000");
        when(authorMock.getPhoto()).thenReturn(null);
        authorRepository.save(authorMock);
    }

    @Test
    public void testCreateBook_Success() {
        // Arrange
        String isbn = "123456789";
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
        assertEquals("123456789", createdBook.getIsbn());
        assertEquals("Test Book Title", createdBook.getTitle());
    }


    @Test
    public void testCreateBook_AlreadyExists() {
        // Arrange
        String isbn = "123456789";
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getDescription()).thenReturn("Description of test book");
        when(request.getAuthors()).thenReturn(List.of("author123"));
        when(request.getGenre()).thenReturn("Science Fiction");
        when(request.getPhoto()).thenReturn(null);

        bookService.create(request, isbn);

        // Act & Assert
        assertThrows(ConflictException.class, () -> {
            bookService.create(request, isbn);
        });
    }

    @Test
    public void testCreateBook_GenreNotFound() {
        // Arrange
        String isbn = "123456789";
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getDescription()).thenReturn("Description of test book");
        when(request.getAuthors()).thenReturn(List.of("author123"));
        when(request.getGenre()).thenReturn("Nonexistent Genre");
        when(request.getPhoto()).thenReturn(null);

        // Act & Assert
        assertThrows(ConflictException.class, () -> {
            bookService.create(request, isbn);
        });
    }

    @Test
    public void testUpdateBook_Success() {
        // Arrange
        String isbn = "123456789";
        CreateBookRequest createRequest = mock(CreateBookRequest.class);
        when(createRequest.getTitle()).thenReturn("Initial Title");
        when(createRequest.getDescription()).thenReturn("Description of test book");
        when(createRequest.getAuthors()).thenReturn(List.of("author123"));
        when(createRequest.getGenre()).thenReturn("Science Fiction");
        when(createRequest.getPhoto()).thenReturn(null);

        // Create a book to update
        Book book = bookService.create(createRequest, isbn);

        UpdateBookRequest updateRequest = mock(UpdateBookRequest.class);
        when(updateRequest.getIsbn()).thenReturn(isbn);
        when(updateRequest.getTitle()).thenReturn("Updated Title");

        // Act
        Book updatedBook = bookService.update(updateRequest, "1");

        // Assert
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals(book.getPk(), updatedBook.getPk());
    }

    @Test
    public void testRemoveBookPhoto_Success() {
        // Arrange
        String isbn = "123456789";

        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getDescription()).thenReturn("Description of test book");
        when(request.getAuthors()).thenReturn(List.of("author123"));
        when(request.getGenre()).thenReturn("Science Fiction");
        when(request.getPhoto()).thenReturn(null);

        Book book = bookService.create(request, isbn);

        // Act
        Book updatedBook = bookService.removeBookPhoto(book.getIsbn(), 1);

        // Assert
        assertNull(updatedBook.getPhoto());
    }

    @Test
    public void testFindByGenre_Success() {
        // Arrange
        String isbn = "2233445566";
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
