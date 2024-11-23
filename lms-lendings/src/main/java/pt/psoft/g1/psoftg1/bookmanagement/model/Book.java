package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Book {

    Isbn isbn;
    Long version;

    public void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    public Book(String isbn) {
        setIsbn(isbn);
    }

    public Book() {
        // got ORM only
    }

    public String getIsbn(){
        return this.isbn.toString();
    }

}
