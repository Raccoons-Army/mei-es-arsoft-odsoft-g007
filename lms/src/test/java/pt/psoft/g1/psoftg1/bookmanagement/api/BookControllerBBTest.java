package pt.psoft.g1.psoftg1.bookmanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.RecommendationService;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookControllerBBTest {

    BookService bookService = mock();
    RecommendationService recommendationService = mock();
    LendingService lendingService = mock();
    ConcurrencyService concurrencyService = mock();
    FileStorageService fileStorageService = mock();
    UserService userService = mock();
    ReaderService readerService = mock();
    BookViewMapper bookViewMapper = mock();
    FactoryGenre factoryGenre = mock();
    FactoryAuthor factoryAuthor = mock();

    BookController bookController = new BookController(bookService, recommendationService, lendingService, concurrencyService,
            fileStorageService, userService, readerService, bookViewMapper);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testCreateBook_Success() {

        // Arrange
        String isbn = "9782826012092";
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("How to Rizz Up Girls In The Dungeon");
        request.setGenre("Fiction");
        request.setAuthors(Arrays.asList("Author One", "Author Two"));

        MultipartFile mockFile = mock(MultipartFile.class);
        request.setPhoto(mockFile);

        Book book = new Book(isbn, "Test Title", null, null, factoryGenre, factoryAuthor);
        book.setVersion(1L);

        when(fileStorageService.getRequestPhoto(mockFile)).thenReturn("photo.jpg");
        when(bookService.create(request, isbn)).thenReturn(book);

        BookView bookView = new BookView();
        when(bookViewMapper.toBookView(book)).thenReturn(bookView);

        // Act
        ResponseEntity<BookView> response = bookController.create(request, isbn);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookView, response.getBody());
    }

    @Test
    public void testCreateBook_Failure_BookServiceException() {
        // Arrange
        String isbn = "9782826012092";
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("How to Rizz Up Girls In The Dungeon");
        request.setGenre("Fiction");
        request.setAuthors(Arrays.asList("Author One", "Author Two"));

        MultipartFile mockFile = mock(MultipartFile.class);
        request.setPhoto(mockFile);

        when(fileStorageService.getRequestPhoto(mockFile)).thenReturn("photo.jpg");

        // Simulate an exception from bookService.create
        when(bookService.create(request, isbn)).thenThrow(new RuntimeException("Failed to create book"));

        // Act
        ResponseEntity<BookView> response = bookController.create(request, isbn);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }



}
