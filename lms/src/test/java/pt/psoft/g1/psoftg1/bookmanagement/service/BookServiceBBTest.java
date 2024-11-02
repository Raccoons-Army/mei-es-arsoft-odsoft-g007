package pt.psoft.g1.psoftg1.bookmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceBBTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private AuthorRepository authorRepository;
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
        request = new CreateBookRequest();
        request.setTitle("Test Book Title");
        request.setGenre("Fiction");
        request.setAuthors(List.of("Author One", "Author Two"));
    }

    @Test
    public void testCreateBook_Success() {
        Genre genre = mock();

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.of(genre));

        // Simulate saving the book to return it after save
        Book savedBook = new Book(isbn, request.getTitle(), null, null, factoryGenre, factoryAuthor);
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
