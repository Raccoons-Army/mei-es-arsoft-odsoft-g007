package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoEntityWithPhotoModel;

@Getter
@Setter
@Document(collection = "authors")
public class MongoAuthorModel extends MongoEntityWithPhotoModel {

    @Id
    private String authorNumber;

    private String name;

    private String bio;

    @Version
    private long version;

    protected MongoAuthorModel() {
    }

    public MongoAuthorModel(String authorNumber, String name, String bio, long version, String photoURI) {
        this.authorNumber = authorNumber;
        this.name = name;
        this.bio = bio;
        this.version = version;
        setPhotoInternal(photoURI);
    }
}
