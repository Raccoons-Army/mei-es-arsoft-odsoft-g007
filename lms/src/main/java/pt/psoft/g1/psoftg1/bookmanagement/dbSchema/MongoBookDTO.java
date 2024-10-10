package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Document(collection = "books")
public class MongoBookDTO {

    @Id
    private Long id;

    @Getter
    private String isbn;

    @Getter
    private String title;

    @Getter
    private Genre genre;

    @Getter
    private List<Author> authors;

    @Getter
    private String description;

    public MongoBookDTO(Long id, String isbn, String title, Genre genre, List<Author> authors, String description) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
    }
}
