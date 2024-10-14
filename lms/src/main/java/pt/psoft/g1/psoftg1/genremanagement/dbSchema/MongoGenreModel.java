package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "genres")
public class MongoGenreModel {
    @Id
    private Long id;

    String genre;

    public MongoGenreModel(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public MongoGenreModel(Long id) {
        this.id = id;
    }
}
