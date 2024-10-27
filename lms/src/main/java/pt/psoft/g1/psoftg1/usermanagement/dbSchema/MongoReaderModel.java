package pt.psoft.g1.psoftg1.usermanagement.dbSchema;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

@Getter
@Setter
@Document(collection = "readers")
public class MongoReaderModel extends MongoUserModel {

    protected MongoReaderModel() {
        // for MongoDB ORM only
    }

    public MongoReaderModel(String username, String password) {
        super(username, password);
        this.addAuthority(new Role(Role.READER));
    }

    public static MongoReaderModel newReader(final String username, final String password, final String name) {
        final var reader = new MongoReaderModel(username, password);
        reader.setName(name);
        return reader;
    }
}
