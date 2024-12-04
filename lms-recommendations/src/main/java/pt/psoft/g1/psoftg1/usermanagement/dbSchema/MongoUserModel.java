package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
public class MongoUserModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    @Version
    private Long version;

    private boolean enabled = true;

    @Field("username")
    private String username;

    private Set<Role> authorities = new HashSet<>();

    protected MongoUserModel() {
        // for MongoDB ORM only
    }

    public MongoUserModel(final String username) {
        this.username = username;
    }

    // Factory method
    public static MongoUserModel newUser(final String username) {
        final var u = new MongoUserModel(username);
        return u;
    }

    // Factory method
    public static MongoUserModel newUser(final String username, final String role) {
        var u = newUser(username);
        u.addAuthority(new Role(role));
        return u;
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }
}
