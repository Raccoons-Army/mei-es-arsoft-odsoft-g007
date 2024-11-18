package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReaderTest {
    @Test
    void ensureValidReaderDetailsAreCreated() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);

        assertDoesNotThrow(() -> new ReaderDetails("2024/123", factoryUserDouble));
    }
}
