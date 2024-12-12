package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "librarians")
public class MongoLibrarianModel extends MongoUserModel {

    protected MongoLibrarianModel() {
        // for MongoDB ORM only
    }

    public MongoLibrarianModel(String username, String password) {
        super(username, password);
    }

    public static MongoLibrarianModel newLibrarian(final String username, final String password, final String name) {
        final var librarian = new MongoLibrarianModel(username, password);
        librarian.setName(name);
        return librarian;
    }
}
