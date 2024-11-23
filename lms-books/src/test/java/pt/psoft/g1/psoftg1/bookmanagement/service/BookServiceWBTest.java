package pt.psoft.g1.psoftg1.bookmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.*;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceWBTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private PhotoRepository photoRepository;
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
        Genre genreMock = mock(Genre.class);

        when(request.getTitle()).thenReturn("Test Book Title");
        when(request.getGenre()).thenReturn("Fiction");
        when(request.getAuthors()).thenReturn(List.of("Author One", "Author Two"));
        when(request.getDescription()).thenReturn("Test Book Description");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.of(genreMock));

        when(bookRepository.save(any(Book.class))).thenReturn(any(Book.class));

        // Act
        bookService.create(request, isbn);

        // Verify
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testCreateBook_Failure_BookAlreadyExists() {
        Book bookMock = mock(Book.class);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock)); // Simulate existing book


        assertThrows(ConflictException.class, () -> bookService.create(request, isbn));
    }

    @Test
    public void testCreateBook_Failure_GenreNotFound() {
        when(request.getGenre()).thenReturn("Nonexistent Genre");
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(request.getGenre())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.create(request, isbn));
    }

    @Test
    public void testUpdateBook_Success() {
        String currentVersion = "1";
        UpdateBookRequest request = mock(UpdateBookRequest.class);
        Book book = mock(Book.class);

        when(request.getIsbn()).thenReturn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book updatedBook = bookService.update(request, currentVersion);

        verify(bookRepository, times(1)).save(book);
        assertNotNull(updatedBook);
    }

    @Test
    public void testRemoveBookPhoto_Success() {
        String isbn = "9782826012092";
        Book book = mock(Book.class);
        Photo photoMock = mock(Photo.class);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(book.getPhoto()).thenReturn(photoMock);
        when(photoMock.getPhotoFile()).thenReturn("file1.jpg");
        when(bookRepository.save(book)).thenReturn(book);

        Book updatedBook = bookService.removeBookPhoto(isbn, 1L);

        verify(photoRepository, times(1)).deleteByPhotoFile("file1.jpg");
        assertNotNull(updatedBook);
    }

    @Test
    public void testRemoveBookPhoto_NoPhotoAssigned() {
        String isbn = "9782826012092";
        Book book = mock(Book.class);
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(book.getPhoto()).thenReturn(null);

        assertThrows(NotFoundException.class, () -> bookService.removeBookPhoto(isbn, 1L));
    }

    @Test
    public void testSearchBooks_WithCustomPage() {
        pt.psoft.g1.psoftg1.shared.services.Page page = mock(pt.psoft.g1.psoftg1.shared.services.Page.class);
        SearchBooksQuery query = mock(SearchBooksQuery.class);
        
        when(bookRepository.searchBooks(page, query)).thenReturn(List.of(mock(Book.class)));

        List<Book> books = bookService.searchBooks(page, query);

        assertFalse(books.isEmpty());
        verify(bookRepository, times(1)).searchBooks(page, query);
    }


}
