package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoEntityWithPhotoModel;

import java.util.List;

@Getter
@Setter
@Document(collection = "books")
public class MongoBookModel extends MongoEntityWithPhotoModel {

    @Id
    private String pk;

    private String isbn;

    private String title;

    @DBRef
    private MongoGenreModel genre;

    @DBRef
    private List<MongoAuthorModel> authors;

    private String description;

    @Version
    private Long version;

    public MongoBookModel(String pk, String isbn, String title, MongoGenreModel genre, List<MongoAuthorModel> authors,
                          String description, String photoURI) {
        this.pk = pk;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
        this.setPhotoInternal(photoURI);
    }

    protected MongoBookModel() {
    }
}
