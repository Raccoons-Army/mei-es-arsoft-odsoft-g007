package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "books")
public class MongoTopBookModel {

    @Id
    private String isbn;

    private String title;

    private String genre;

    private List<String> authors;

    private String description;

    private long lendingCount;

    @Version
    private Long version;

    public MongoTopBookModel(String isbn, String title, String genre, List<String> authors,
                             String description, long lendingCount) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
        this.lendingCount = lendingCount;
    }

    protected MongoTopBookModel() {
    }
}
