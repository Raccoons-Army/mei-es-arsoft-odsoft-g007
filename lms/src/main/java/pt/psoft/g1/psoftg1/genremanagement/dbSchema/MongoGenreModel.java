package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "genres")
public class MongoGenreModel {

    @Id
    private String pk;

    String genre;

    public MongoGenreModel(String pk, String genre) {
        this.pk = pk;
        this.genre = genre;
    }

    protected MongoGenreModel() {
    }
}
