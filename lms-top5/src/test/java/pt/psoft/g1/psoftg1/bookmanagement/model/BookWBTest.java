package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;

import static org.mockito.Mockito.*;

public class BookWBTest {

    private final String validIsbn = "9782826012092";
    private final String validTitle = "Encantos de contar";

    @Test
    void verifyAddAuthorIsCalledOneTime() throws InstantiationException {
        Author authorDouble = mock(Author.class);

        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        when(factoryAuthorDouble.newAuthor("aaa1", "A1"))
                .thenReturn(authorDouble);

        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);
        book.addAuthor("aaa1", "A1");

        // verify
        verify(factoryAuthorDouble, times(1)).newAuthor("aaa1", "A1");

    }

    @Test
    void verifyAddAuthorIsCalledNTimes() throws InstantiationException {
        final int n = 10;

        Author authorDouble = mock(Author.class);

        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);
        FactoryAuthor factoryAuthorDouble = mock(FactoryAuthor.class);

        when(factoryAuthorDouble.newAuthor("aaa1", "A1"))
                .thenReturn(authorDouble);

        Book book = new Book(validIsbn, validTitle, null, factoryGenreDouble, factoryAuthorDouble);

        for (int i = 0; i< n; i++) {
            book.addAuthor("aaa1", "A1");
        }

        // verify
        verify(factoryAuthorDouble, times(n)).newAuthor("aaa1", "A1");
    }
}
