package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "ForbiddenName")
public class JpaForbiddenNameModel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @NotNull
    private String photoFile;

    public JpaForbiddenNameModel(long id, String photoFile) {
        this.id = id;
        this.photoFile = photoFile;
    }

    public JpaForbiddenNameModel() {}
}
