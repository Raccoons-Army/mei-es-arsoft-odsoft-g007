package pt.psoft.g1.psoftg1.shared.dbSchema;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "photos")
public class MongoForbiddenNameModel {

    @Id
    private Long id;

    private String photoFile;

    public MongoForbiddenNameModel(Long id, String photoFile) {
        this.id = id;
        this.photoFile = photoFile;
    }


}
