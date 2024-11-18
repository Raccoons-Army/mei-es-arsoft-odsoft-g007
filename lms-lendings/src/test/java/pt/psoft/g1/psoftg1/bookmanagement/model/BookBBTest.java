package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookBBTest {
    private final String validIsbn = "9782826012092";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testBookCreation() {
        Book book = new Book(validIsbn);
        assertEquals("9782826012092", book.getIsbn());
    }

    @Test
    void ensureIsbnNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null));
    }

    @Test
    void ensureIsbnNotEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Book(""));
    }

    @Test
    void ensureIsbnNotTooLong() {
        assertThrows(IllegalArgumentException.class, () -> new Book("97833333333333333333333333222228260120921"));
    }

}