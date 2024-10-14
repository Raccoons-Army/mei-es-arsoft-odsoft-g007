package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorDTO;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Getter
@Document(collection = "books")
public class MongoBookDTO {

    @Id
    private String id;

    private String isbn;

    private String title;

    private Long genre;

    private List<Long> authors;

    private String description;

    public MongoBookDTO(String id, String isbn, String title, Long genre, List<Long> authors, String description) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
    }
}
