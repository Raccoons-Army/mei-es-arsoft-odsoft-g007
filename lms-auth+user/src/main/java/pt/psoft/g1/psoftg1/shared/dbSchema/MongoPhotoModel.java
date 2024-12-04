package pt.psoft.g1.psoftg1.shared.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.file.Path;

@Getter
@Setter
@Document(collection = "photos")
public class MongoPhotoModel {

    @Id
    private String pk;

    @Getter
    private String photoFile;

    public MongoPhotoModel(String photoPath) {
        this.photoFile = photoPath;
    }

    protected MongoPhotoModel() {
    }


}
