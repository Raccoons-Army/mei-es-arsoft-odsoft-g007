package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "authors")
public class MongoAuthorDTO {

    @Id
    private Long authorNumber;

    private String name;

    private String bio;

    public MongoAuthorDTO(Long authorNumber, String name, String bio) {
        this.authorNumber = authorNumber;
        this.name = name;
        this.bio = bio;
    }

    public MongoAuthorDTO(Long authorNumber) {
        this.authorNumber = authorNumber;
    }

    public MongoAuthorDTO() {
    }
}
