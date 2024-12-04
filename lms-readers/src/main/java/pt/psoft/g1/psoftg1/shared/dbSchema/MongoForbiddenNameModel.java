package pt.psoft.g1.psoftg1.shared.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "forbiddenNames")
public class MongoForbiddenNameModel {

    @Id
    private String pk;

    private String forbiddenName;

    public MongoForbiddenNameModel(String forbiddenName) {
        this.forbiddenName = forbiddenName;
    }

    protected MongoForbiddenNameModel() {
    }
}