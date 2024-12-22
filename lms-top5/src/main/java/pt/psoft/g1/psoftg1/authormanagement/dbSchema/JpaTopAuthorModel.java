package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "TopAuthor")
public class JpaTopAuthorModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    String pk;

    @Version
    private long version;

    @NotNull
    @NotBlank
    @Column(name="NAME", length = 150)
    private String name;

    private long lendingCount;

    protected JpaTopAuthorModel() {
    }

    public JpaTopAuthorModel(long version, String name, long lendingCount) {
        this.version = version;
        this.name = name;
        this.lendingCount = lendingCount;
    }
}
