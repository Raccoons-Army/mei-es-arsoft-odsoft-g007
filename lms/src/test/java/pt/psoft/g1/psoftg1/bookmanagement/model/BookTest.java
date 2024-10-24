package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookTest {
    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";

    @BeforeEach
    void setUp(){}

    @Test
    void ensureIsbnNotNull(){
        FactoryGenre doubleFactoryGenre = mock(FactoryGenre.class);
        FactoryAuthor doubleFactoryAuthor = mock(FactoryAuthor.class);

        assertThrows(IllegalArgumentException.class, () -> new Book(null, validTitle, null, null, doubleFactoryGenre, doubleFactoryAuthor));
    }

    @Test
    void ensureTitleNotNull(){
        FactoryGenre doubleFactoryGenre = mock(FactoryGenre.class);
        FactoryAuthor doubleFactoryAuthor = mock(FactoryAuthor.class);

        assertThrows(IllegalArgumentException.class, () -> new Book(validIsbn, null, null, null, doubleFactoryGenre, doubleFactoryAuthor));
    }

    @Test
    void ensureGenreNotNull() throws InstantiationException {
        FactoryGenre doubleFactoryGenre = mock(FactoryGenre.class);
        FactoryAuthor doubleFactoryAuthor = mock(FactoryAuthor.class);

        String expectedMessage = "Invalid arguments";
        when(doubleFactoryGenre.newGenre(null)).thenThrow(new InstantiationException(expectedMessage));

        Book book = new Book(validIsbn, validTitle, null, null, doubleFactoryGenre, doubleFactoryAuthor);

        assertThrows(InstantiationException.class, () -> book.defineGenre(null));
    }

    @Test
    void ensureAuthorsNotNull() throws InstantiationException {
        FactoryGenre doubleFactoryGenre = mock(FactoryGenre.class);
        FactoryAuthor doubleFactoryAuthor = mock(FactoryAuthor.class);

        String expectedMessage = "Invalid arguments";
        when(doubleFactoryAuthor.newAuthor(null, null, null, null)).thenThrow(new InstantiationException(expectedMessage));

        Book book = new Book(validIsbn, validTitle, null, null, doubleFactoryGenre, doubleFactoryAuthor);

        assertThrows(InstantiationException.class, () -> book.addAuthor(null, null, null, null));
        assertEquals(book.getAuthors().size(), 0);
    }

    @Test
    void ensureBookCreatedWithMultipleAuthors() throws InstantiationException {
        FactoryGenre doubleFactoryGenre = mock(FactoryGenre.class);
        FactoryAuthor doubleFactoryAuthor = mock(FactoryAuthor.class);

        Book book = new Book(validIsbn, validTitle, null, null, doubleFactoryGenre, doubleFactoryAuthor);
        book.addAuthor("aaa1", "João Alberto", "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", null);
        book.addAuthor("aaa2","Maria José", "A Maria José nasceu em Viseu e só come laranjas às segundas feiras.", null);

        assertEquals(book.getAuthors().size(), 2);
    }

}