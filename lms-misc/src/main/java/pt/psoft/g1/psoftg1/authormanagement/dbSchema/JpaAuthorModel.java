package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.*;
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

    protected JpaAuthorModel() {
    }

    public JpaAuthorModel(String authorNumber, long version) {
        this.authorNumber = authorNumber;
        this.version = version;
    }
}
