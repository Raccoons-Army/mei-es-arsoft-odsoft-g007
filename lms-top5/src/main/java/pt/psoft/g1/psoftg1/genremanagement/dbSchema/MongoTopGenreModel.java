package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "genres")
public class MongoTopGenreModel {

    @Id
    String genre;

    long lendingCount;

    public MongoTopGenreModel( String genre, long lendingCount) {
        this.genre = genre;
        this.lendingCount = lendingCount;
    }

    protected MongoTopGenreModel() {
    }
}
