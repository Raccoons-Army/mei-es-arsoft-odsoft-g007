package pt.psoft.g1.psoftg1.suggestedbookmanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;

@Setter
@Getter
public class SuggestedBook {

    private String pk;

    private Isbn isbn;

    public SuggestedBook(String isbn) {
        setIsbn(isbn);
    }

    public void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }
}
