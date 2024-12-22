package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "authors")
public class MongoTopAuthorModel {

    @Id
    private String pk;

    private String name;

    private long lendingCount;

    @Version
    private long version;

    protected MongoTopAuthorModel() {
    }

    public MongoTopAuthorModel(String pk, String name, long lendingCount, long version) {
        this.pk = pk;
        this.name = name;
        this.lendingCount = lendingCount;
        this.version = version;
    }
}
