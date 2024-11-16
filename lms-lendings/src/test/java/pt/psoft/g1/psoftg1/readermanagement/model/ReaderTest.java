package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReaderTest {
    @Test
    void ensureValidReaderDetailsAreCreated() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        assertDoesNotThrow(() -> new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,null, factoryUserDouble, factoryGenreDouble));
    }

    @Test
    void ensureExceptionIsThrownForNullPhoneNumber() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails("123", "2010-01-01", null, true, false, false,null,factoryUserDouble, factoryGenreDouble));
    }

    @Test
    void ensureExceptionIsThrownForNoGdprConsent() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails("123", "2010-01-01", "912345678", false, false, false,null,factoryUserDouble, factoryGenreDouble));
    }

    @Test
    void ensureGdprConsentIsTrue() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,null,factoryUserDouble, factoryGenreDouble);
        assertTrue(readerDetails.isGdprConsent());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,null,factoryUserDouble, factoryGenreDouble);
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",factoryUserDouble, factoryGenreDouble);
        Photo photo = readerDetails.getPhoto();

        //This is here to force the test to fail if the photo is null
        assertNotNull(photo);

        String readerPhoto = photo.getPhotoFile();
        assertEquals("readerPhotoTest.jpg", readerPhoto);
    }

    @Test
    void ensureInterestListCanBeEmptyList_AkaOptional() {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",factoryUserDouble, factoryGenreDouble);
        assertEquals(0, readerDetailsInterestListEmpty.getInterestList().size());
    }

    @Test
    void ensureInterestListCanTakeAnyValidGenre() throws InstantiationException {
        FactoryUser factoryUserDouble = mock(FactoryUser.class);
        FactoryGenre factoryGenreDouble = mock(FactoryGenre.class);

        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",factoryUserDouble, factoryGenreDouble);
        readerDetails.addGenre("genre1");
        readerDetails.addGenre("genre2");

        assertEquals(2, readerDetails.getInterestList().size());
    }
}
