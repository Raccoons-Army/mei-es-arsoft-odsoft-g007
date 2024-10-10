package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Document(collection = "books")
public class MongoBookDTO {

    @Id
    private String id;

    private String isbn;
    private String title;
    private Genre genre;
    private List<Author> authors;
    private String description;
}
