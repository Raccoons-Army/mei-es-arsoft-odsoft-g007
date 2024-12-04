package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderTest {

    private final String username = "onee_chan@gmail.com";
    private final List<String> interestList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        interestList.add("Infantil");
        interestList.add("Manga");
    }


    @Test
    void ensureValidReaderDetailsAreCreated() {
        assertDoesNotThrow(() -> new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,null, username, interestList));
    }

    @Test
    void ensureExceptionIsThrownForNullPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails("123", "2010-01-01", null, true, false, false,null,username, interestList));
    }

    @Test
    void ensureExceptionIsThrownForNoGdprConsent() {
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails("123", "2010-01-01", "912345678", false, false, false,null,username, interestList));
    }

    @Test
    void ensureGdprConsentIsTrue() {
        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,null,username, interestList);
        assertTrue(readerDetails.isGdprConsent());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,null,username, interestList);
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",username, interestList);
        Photo photo = readerDetails.getPhoto();

        //This is here to force the test to fail if the photo is null
        assertNotNull(photo);

        String readerPhoto = photo.getPhotoFile();
        assertEquals("readerPhotoTest.jpg", readerPhoto);
    }

    @Test
    void ensureInterestListCanBeEmptyList_AkaOptional() {
        List<String> emptyInterestList = new ArrayList<>();
        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",username, emptyInterestList);
        assertEquals(0, readerDetailsInterestListEmpty.getInterestList().size());
    }

    @Test
    void ensureInterestListCanTakeAnyValidGenre() throws InstantiationException {
        ReaderDetails readerDetails = new ReaderDetails("123", "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",username, interestList);
        assertEquals(2, readerDetails.getInterestList().size());
    }
}
