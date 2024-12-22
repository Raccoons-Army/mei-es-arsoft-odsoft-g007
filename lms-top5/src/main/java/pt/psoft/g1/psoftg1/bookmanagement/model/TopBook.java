package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TopBook {
    private String isbn;
    private String title;
    private String genre;
    private String description;
    private List<String> authors;
    private long lendingCount;

    public TopBook(String isbn, String title, String genre, String description, List<String> authors, long lendingCount) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.authors = authors;
        this.lendingCount = lendingCount;
    }
}
