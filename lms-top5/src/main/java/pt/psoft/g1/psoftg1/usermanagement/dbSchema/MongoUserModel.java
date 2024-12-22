package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
public class MongoUserModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    private String username;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    private Set<Role> authorities = new HashSet<>();

    protected MongoUserModel() {
        // for MongoDB ORM only
    }

    public MongoUserModel(final String username) {
        this.username = username;
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }
}
