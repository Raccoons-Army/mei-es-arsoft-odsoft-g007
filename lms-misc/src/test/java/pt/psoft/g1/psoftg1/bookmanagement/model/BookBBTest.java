package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
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

        Book book = new Book(validIsbn, validTitle, "Sample Description", factoryGenreDouble, factoryAuthorDouble);

        assertEquals("9782826012092", book.getIsbn());
        assertEquals("Encantos de contar", book.getTitle());
        assertEquals("Sample Description", book.getDescription());
    }

    @Test
    void ensureIsbnNotNull() {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, factoryGenreDouble, factoryAuthorDouble));
    }

    @Test
    void ensureTitleNotNull() {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        assertThrows(NullPointerException.class, () -> new Book(validIsbn, null, null, factoryGenreDouble, factoryAuthorDouble));
    }

    @Test
    void ensureGenreNotNull() throws InstantiationException {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        String expectedMessage = "Invalid arguments";
        when(factoryGenreDouble.newGenre(null)).thenThrow(new InstantiationException(expectedMessage));

        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);

        assertThrows(InstantiationException.class, () -> book.defineGenre(null));
    }

    @Test
    void testSetAndGetGenre() throws InstantiationException {
        Genre mockGenre = mock(Genre.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);
        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);

        when(factoryGenreDouble.newGenre("Fiction")).thenReturn(mockGenre);

        book.defineGenre("Fiction");

        assertEquals(mockGenre, book.getGenre());
    }

    @Test
    void ensureAuthorsNotNull() throws InstantiationException {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        String expectedMessage = "Invalid arguments";
        when(factoryAuthorDouble.newAuthor(null)).thenThrow(new InstantiationException(expectedMessage));

        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);

        assertThrows(InstantiationException.class, () -> book.addAuthor(null));
        assertEquals(book.getAuthors().size(), 0);
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() throws InstantiationException {
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);
        book.addAuthor("aaa1");
        book.addAuthor("aaa2");

        assertEquals(book.getAuthors().size(), 2);
    }

    @Test
    void whenValidAuthorData_thenAuthorIsCreatedAndAdded() throws InstantiationException {
        Author authorDouble = mock(Author.class);

        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        when(factoryAuthorDouble.newAuthor("aaa1"))
                .thenReturn(authorDouble);

        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);
        book.addAuthor("aaa1");

        // assert
        assertEquals(book.getAuthors().size(), 1);
    }
}