package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "T_USER")
@EntityListeners(AuditingEntityListener.class)
public class JpaUserModel {

    private static final long serialVersionUID = 1L;

    // database primary key
    @Id
    @GeneratedValue
    @Column(name="USER_ID")
    private Long pk;

    // optimistic lock concurrency control
    @Version
    private Long version;

    // auditing info
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // auditing info
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    // auditing info
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    // auditing info
    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy;

    private boolean enabled = true;

    @Column(unique = true, /*updatable = false,*/ nullable = false)
    @Email
    @NotNull
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String password;

    private String name;

    @ElementCollection
    private final Set<Role> authorities = new HashSet<>();

    protected JpaUserModel() {
        // for ORM only
    }

    /**
     *
     * @param username
     * @param password
     */
    public JpaUserModel(final String username, final String password) {
        this.username = username;
        setPassword(password);
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @param password
     * @param name
     * @return
     */
    public static JpaUserModel newUser(final String username, final String password, final String name) {
        final var u = new JpaUserModel(username, password);
        u.setName(name);
        return u;
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @param password
     * @param name
     * @param role
     * @return
     */
    public static JpaUserModel newUser(final String username, final String password, final String name, final String role) {
        final var u = new JpaUserModel(username, password);
        u.setName(name);
        u.addAuthority(new Role(role));
        return u;
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

}
