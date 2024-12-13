package pt.psoft.g1.psoftg1.suggestionmanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;

@Setter
@Getter
public class Suggestion {

    private String pk;

    private Isbn suggestedBook;

    private LocalDate instantDate;

    private ReaderDetails reader;

    private long version;

    public Suggestion(String id, String isbn, LocalDate instantDate, ReaderDetails reader) {
        this.pk = id;
        setIsbn(isbn);
        this.instantDate = instantDate;
        this.reader = reader;
    }

    public void setIsbn(String isbn) {
        this.suggestedBook = new Isbn(isbn);
    }
}
