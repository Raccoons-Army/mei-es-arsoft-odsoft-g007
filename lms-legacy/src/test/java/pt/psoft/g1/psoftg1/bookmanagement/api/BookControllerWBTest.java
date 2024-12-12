package pt.psoft.g1.psoftg1.bookmanagement.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.RecommendationService;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class BookControllerWBTest {

    private final BookService bookService = mock(BookService.class);
    private final RecommendationService recommendationService = mock(RecommendationService.class);
    private final LendingService lendingService = mock(LendingService.class);
    private final ConcurrencyService concurrencyService = mock(ConcurrencyService.class);
    private final FileStorageService fileStorageService = mock(FileStorageService.class);
    private final UserService userService = mock(UserService.class);
    private final ReaderService readerService = mock(ReaderService.class);
    private final BookViewMapper bookViewMapper = mock(BookViewMapper.class);

    BookController bookController = new BookController(bookService, recommendationService, lendingService, concurrencyService,
            fileStorageService, userService, readerService, bookViewMapper);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    // Successfully creates a book
    @Test
    public void testCreateBookSuccess() {
        // Mock data
        CreateBookRequest createBookRequest = new CreateBookRequest();
        String isbn = "1234567890";
        MultipartFile photo = mock(MultipartFile.class);
        createBookRequest.setPhoto(photo);
        Book bookMock = mock(Book.class);
        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getVersion()).thenReturn(1L);

        // Mock behavior
        when(fileStorageService.getRequestPhoto(photo)).thenReturn("photo.jpg");
        when(bookService.create(createBookRequest, isbn)).thenReturn(bookMock);

        bookController.create(createBookRequest, isbn);

        // Verify
        verify(fileStorageService, times(1)).getRequestPhoto(photo);
        verify(bookService, times(1)).create(createBookRequest, isbn);
    }

    // Throws exception when trying to create a book with an existing ISBN
    @Test
    void testCreateBookThrowsException() {
        CreateBookRequest createRequest = new CreateBookRequest();
        String isbn = "repeated_isbn";

        // Simulate an error in book creation
        when(bookService.create(createRequest, isbn)).thenThrow(new ConflictException("Book already exists"));

        bookController.create(createRequest, isbn);

        // Verify
        verify(bookService, times(1)).create(createRequest, isbn);
    }

    // Successfully retrieves a list of books
    @Test
    public void testFindByIsbnExistingBook() {

        Book bookMock = mock(Book.class);
        String validIsbn = "1234567890";
        BookView bookViewMock = mock(BookView.class);

        when(bookService.findByIsbn(validIsbn)).thenReturn(bookMock);
        when(bookViewMapper.toBookView(bookMock)).thenReturn(bookViewMock);
        when(bookMock.getIsbn()).thenReturn(validIsbn);

        bookController.findByIsbn(validIsbn);

        // verify
        verify(bookService, times(1)).findByIsbn(validIsbn);
        verify(bookViewMapper, times(1)).toBookView(bookMock);
    }

    // Throws exception when trying to find a non-existing book
    @Test
    public void testFindByIsbnNonExistingBook() {
        Book bookMock = mock(Book.class);
        BookView bookViewMock = mock(BookView.class);
        String invalidIsbn = "1234567890";

        when(bookService.findByIsbn(invalidIsbn)).thenThrow(new NotFoundException("Book not found"));

        assertThrows(NotFoundException.class, () -> bookController.findByIsbn(invalidIsbn));

        // verify
        verify(bookService, times(1)).findByIsbn(invalidIsbn);
        verify(bookViewMapper, times(0)).toBookView(bookMock);
    }

    // Successfully deletes a photo from an existing book
    @Test
    public void deleteBookPhotoSuccess() {
        String isbn = "1234567890";
        String photoFile = "photo.jpg";
        Book bookMock = mock(Book.class);
        Photo photoMock = mock(Photo.class);

        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getVersion()).thenReturn(1L);
        when(bookMock.getPhoto()).thenReturn(photoMock);
        when(photoMock.getPhotoFile()).thenReturn(photoFile);

        when(bookService.findByIsbn(isbn)).thenReturn(bookMock);
        when(bookService.removeBookPhoto(bookMock.getIsbn(), bookMock.getVersion())).thenReturn(bookMock);
        doNothing().when(fileStorageService).deleteFile(photoFile);

        bookController.deleteBookPhoto(isbn);

        // verify
        verify(bookService, times(1)).findByIsbn(isbn);
        verify(bookService, times(1)).removeBookPhoto(bookMock.getIsbn(), bookMock.getVersion());
        verify(fileStorageService, times(1)).deleteFile(photoFile);
    }

    // Throws exception when trying to delete a photo from a non-existing book
    @Test
    public void deleteBookPhotoThrowsException() {
        String isbn = "1234567890";
        Book bookMock = mock(Book.class);
        when(bookService.findByIsbn(isbn)).thenThrow(new NotFoundException("Book not found"));

        assertThrows(NotFoundException.class, () -> bookController.deleteBookPhoto(isbn));

        // verify
        verify(bookService, times(1)).findByIsbn(isbn);
        verify(bookService, times(0)).removeBookPhoto(isbn, bookMock.getVersion());
    }

    // Successfully retrieves recommendations for authenticated users with valid reader details
    @Test
    public void testRecommendationsSuccess() {
        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);
        ReaderDetails readerDetails = mock(ReaderDetails.class);
        Book bookMock1 = mock(Book.class);
        Book bookMock2 = mock(Book.class);
        List<Book> recommendedBooks = List.of(bookMock1, bookMock2);
        BookView bookViewMock1 = mock(BookView.class);
        BookView bookViewMock2 = mock(BookView.class);
        List<BookView> bookViews = List.of(bookViewMock1, bookViewMock2);
        String username = "testUser";
        String readerNumber = "12345";
        String pk = "12345";

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.getAuthenticatedUser(authentication)).thenReturn(user);
        when(readerService.findByUsername(username)).thenReturn(Optional.of(readerDetails));
        when(readerDetails.getReaderNumber()).thenReturn(readerNumber);
        when(readerDetails.getPk()).thenReturn(pk);
        when(user.getUsername()).thenReturn(username);
        when(recommendationService.getRecommendationsForReader(readerNumber)).thenReturn(recommendedBooks);
        when(bookViewMapper.toBookView(recommendedBooks)).thenReturn(bookViews);

        bookController.getRecommendations(authentication);

//        assertEquals(2, response.getItems().size());

        // verify authentication, find user and get recommendations
        verify(userService, times(1)).getAuthenticatedUser(authentication);
        verify(readerService, times(1)).findByUsername(username);
        verify(recommendationService, times(1)).getRecommendationsForReader("12345");
    }

    // Throws exception when trying to retrieve recommendations for an authenticated user with invalid reader details
    @Test
    public void testRecommendationsThrowsException() {
        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);
        ReaderDetails readerDetails = mock(ReaderDetails.class);
        String username = "testUser";
        String readerNumber = "12345";

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.getAuthenticatedUser(authentication)).thenReturn(user);
        when(readerService.findByUsername(username)).thenReturn(Optional.of(readerDetails));
        when(readerDetails.getReaderNumber()).thenReturn(readerNumber);
        when(recommendationService.getRecommendationsForReader(readerNumber)).thenThrow(new NotFoundException("No recommendations found"));

        assertThrows(NotFoundException.class, () -> bookController.getRecommendations(authentication));

        // verify
        verify(userService, times(1)).getAuthenticatedUser(authentication);
    }
}
