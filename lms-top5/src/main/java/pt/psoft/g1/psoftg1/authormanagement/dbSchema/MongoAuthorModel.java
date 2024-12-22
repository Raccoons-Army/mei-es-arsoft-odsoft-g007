package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "authors")
public class MongoAuthorModel {

    @Id
    private String authorNumber;

    private String name;

    @Version
    private long version;

    protected MongoAuthorModel() {
    }

    public MongoAuthorModel(String authorNumber, String name, long version) {
        this.authorNumber = authorNumber;
        this.name = name;
        this.version = version;
    }
}
