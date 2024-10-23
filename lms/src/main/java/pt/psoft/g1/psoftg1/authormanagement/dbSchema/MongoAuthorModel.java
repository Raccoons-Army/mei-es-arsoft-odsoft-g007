package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "authors")
public class MongoAuthorModel {

    @Id
    private Long authorNumber;

    private String name;

    private String bio;

    public MongoAuthorModel(Long authorNumber, String name, String bio) {
        this.authorNumber = authorNumber;
        this.name = name;
        this.bio = bio;
    }

    public MongoAuthorModel(Long authorNumber) {
        this.authorNumber = authorNumber;
    }

    public MongoAuthorModel() {
    }
}
