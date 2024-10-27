package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "T_USERS")
public class MongoUserModel {

    @Id
    private Long pk;

    private Long version;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("modified_at")
    private LocalDateTime modifiedAt;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @LastModifiedBy
    @Field("modified_by")
    private String modifiedBy;

    private boolean enabled = true;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    private String name;

    private final Set<Role> authorities = new HashSet<>();

    protected MongoUserModel() {
        // for MongoDB ORM only
    }

    public MongoUserModel(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    // Factory method
    public static MongoUserModel newUser(final String username, final String password, final String name) {
        final var u = new MongoUserModel(username, password);
        u.setName(name);
        return u;
    }

    // Factory method
    public static MongoUserModel newUser(final String username, final String password, final String name, final String role) {
        var u = newUser(username, password, name);
        u.addAuthority(new Role(role));
        return u;
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }
}
