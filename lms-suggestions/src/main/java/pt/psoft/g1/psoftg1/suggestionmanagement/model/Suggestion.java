package pt.psoft.g1.psoftg1.suggestionmanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;

@Setter
@Getter
public class Suggestion {

    private String pk;

    private Isbn isbn;

    private LocalDate createdAt;

    private ReaderDetails readerDetails;

    private long version;

    public Suggestion(String id, String isbn, LocalDate instantDate, ReaderDetails reader) {
        this.pk = id;
        setIsbn(isbn);
        this.createdAt = instantDate;
        this.readerDetails = reader;
    }

    public void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    public String getIsbn(){
        return this.isbn.toString();
    }
}
