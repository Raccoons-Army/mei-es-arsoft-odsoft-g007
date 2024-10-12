package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "genres")
public class MongoGenreDTO {
    @Id
    private Long id;

    String genre;

    public MongoGenreDTO(Long id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public MongoGenreDTO(Long id) {
        this.id = id;
    }
}
