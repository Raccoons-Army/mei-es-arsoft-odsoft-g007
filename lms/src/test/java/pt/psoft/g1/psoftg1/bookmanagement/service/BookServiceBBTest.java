package pt.psoft.g1.psoftg1.bookmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookServiceImpl;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceBBTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private FactoryGenre factoryGenre;
    @Mock
    private FactoryAuthor factoryAuthor;

    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequest request;
    private final String isbn = "9782826012092";

    @BeforeEach
    public void setup() {
        request = mock(CreateBookRequest.class);
    }

    @Test
    public void testCreateBook_Success() {
        Genre genreMock = mock();
        Author authorMock = mock(Author.class);
        Photo photoMock = mock(Photo.class);

        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getGenre()).thenReturn("Fiction");
        when(request.getAuthors()).thenReturn(List.of("Author One", "Author Two"));

        Book savedBook = mock(Book.class);
        when(savedBook.getIsbn()).thenReturn(isbn);

        when(authorMock.getPhoto()).thenReturn(photoMock);

        // Mocking repository and request interactions
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.of(genreMock));
        when(authorRepository.findByAuthorNumber("Author One")).thenReturn(Optional.of(authorMock));
        when(authorRepository.findByAuthorNumber("Author Two")).thenReturn(Optional.of(authorMock));

        // Simulate saving the book to return it after save
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        Book result = bookService.create(request, isbn);

        // Assert
        assertNotNull(result);
        assertEquals(isbn, result.getIsbn());
    }

    @Test
    public void testCreateBook_Failure_BookAlreadyExists() {
        Book book = mock(Book.class);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book)); // Simulate existing book

        assertThrows(ConflictException.class, () -> bookService.create(request, isbn));
    }


}
