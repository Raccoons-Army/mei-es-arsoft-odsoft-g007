package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.file.Path;

@Getter
@Document(collection = "photos")
public class MongoPhotoModel {

    @Id
    private Long pk;

    @Getter
    @Setter
    @NotNull
    private String photoFile;

    public MongoPhotoModel(Path photoPath) {
        setPhotoFile(photoPath.toString());
    }

    protected MongoPhotoModel() {
    }


}
