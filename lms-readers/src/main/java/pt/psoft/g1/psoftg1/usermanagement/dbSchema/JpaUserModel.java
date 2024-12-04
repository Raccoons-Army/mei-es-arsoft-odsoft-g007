package pt.psoft.g1.psoftg1.usermanagement.dbSchema;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "APP_USER")
@EntityListeners(AuditingEntityListener.class)
public class JpaUserModel implements Serializable {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @Column(name="USER_ID")
    private String pk;

    private String username;

    // auditing info
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "USER_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"))
    private final Set<Role> authorities = new HashSet<>();

    protected JpaUserModel() {
        // for ORM only
    }

    public JpaUserModel(final String username) {
        this.username = username;
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

}
