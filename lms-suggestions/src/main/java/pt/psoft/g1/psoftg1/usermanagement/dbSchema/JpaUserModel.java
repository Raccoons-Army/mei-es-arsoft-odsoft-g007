package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
public class JpaUserModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // database primary key
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(name="USER_ID")
    private String pk;

    // optimistic lock concurrency control
    @Version
    private Long version;

    private boolean enabled = true;

    @Column(unique = true, nullable = false)
    @Email
    @NotNull
    @NotBlank
    private String username;

    @ElementCollection
    @CollectionTable(name = "USER_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_USER_ID"))
    private final Set<Role> authorities = new HashSet<>();

    protected JpaUserModel() {
        // for ORM only
    }

    public JpaUserModel(final String username) {
        this.username = username;
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @return
     */
    public static JpaUserModel newUser(final String username) {
        return new JpaUserModel(username);
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @param role
     * @return
     */
    public static JpaUserModel newUser(final String username, final String role) {
        var u = newUser(username);
        u.addAuthority(new Role(role));
        return u;
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

}
