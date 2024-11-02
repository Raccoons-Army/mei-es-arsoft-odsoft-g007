package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookBBTest {
    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testBookCreation() {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        Book book = new Book(validIsbn, validTitle, "Any", null, factoryGenreDouble, factoryAuthorDouble);

        assertEquals("9782826012092", book.getIsbn());
        assertEquals("Encantos de contar", book.getTitle());
        assertEquals("Sample Description", book.getDescription());
        assertEquals("Any", book.getDescription());
    }

    @Test
    void ensureIsbnNotNull() {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble));
    }

    @Test
    void ensureTitleNotNull() {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        assertThrows(NullPointerException.class, () -> new Book(validIsbn, null, null, null, factoryGenreDouble, factoryAuthorDouble));
    }

    @Test
    void ensureGenreNotNull() throws InstantiationException {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        String expectedMessage = "Invalid arguments";
        when(factoryGenreDouble.newGenre(null)).thenThrow(new InstantiationException(expectedMessage));

        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);

        assertThrows(InstantiationException.class, () -> book.defineGenre(null));
    }

    @Test
    void testSetAndGetGenre() throws InstantiationException {
        Genre mockGenre = mock(Genre.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);
        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);

        when(factoryGenreDouble.newGenre("Fiction")).thenReturn(mockGenre);

        book.defineGenre("Fiction");

        assertEquals(mockGenre, book.getGenre());
    }

    @Test
    void ensureAuthorsNotNull() throws InstantiationException {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        String expectedMessage = "Invalid arguments";
        when(factoryAuthorDouble.newAuthor(null, null, null, null)).thenThrow(new InstantiationException(expectedMessage));

        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);

        assertThrows(InstantiationException.class, () -> book.addAuthor(null, null, null, null));
        assertEquals(book.getAuthors().size(), 0);
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() throws InstantiationException {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);
        book.addAuthor("aaa1", "João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
        book.addAuthor("aaa2", "Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);

        assertEquals(book.getAuthors().size(), 2);
    }

    @Test
    void whenValidAuthorData_thenAuthorIsCreatedAndAdded() throws InstantiationException {
        Author authorDouble = mock(Author.class);

        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        when(factoryAuthorDouble.newAuthor("aaa1", "João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null))
                .thenReturn(authorDouble);

        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);
        book.addAuthor("aaa1", "João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);

        // assert
        assertEquals(book.getAuthors().size(), 1);
    }

    @Test
    void testApplyPatchWithSuccess() {
        UpdateBookRequest mockRequest = mock(UpdateBookRequest.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        when(mockRequest.getTitle()).thenReturn("Updated Title");
        when(mockRequest.getDescription()).thenReturn("Updated Description");

        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);

        book.setVersion(1L);
        book.applyPatch(1L, mockRequest);

        assertEquals("Updated Title", book.getTitle());
        assertEquals("Updated Description", book.getDescription());
    }

    @Test
    void testApplyPatchWithStaleVersionThrowsException() {
        UpdateBookRequest mockRequest = mock(UpdateBookRequest.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        Book book = new Book(validIsbn, validTitle, null, null, factoryGenreDouble, factoryAuthorDouble);

        book.setVersion(2L);

        assertThrows(StaleObjectStateException.class, () -> book.applyPatch(1L, mockRequest));
    }


}