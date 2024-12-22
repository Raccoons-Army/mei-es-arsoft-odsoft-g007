package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Author")
public class JpaAuthorModel {

    @Id
    @Column(name = "AUTHOR_NUMBER")
    private String authorNumber;

    @Version
    private long version;

    @NotNull
    @NotBlank
    @Column(name="NAME", length = 150)
    private String name;

    protected JpaAuthorModel() {
    }

    public JpaAuthorModel(String authorNumber, long version, String name) {
        this.authorNumber = authorNumber;
        this.version = version;
        this.name = name;
    }
}
