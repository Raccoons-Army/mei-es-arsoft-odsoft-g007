package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Forbidden_Name")
public class JpaForbiddenNameModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @Column(nullable = false)
    @Size(min = 1)
    private String forbiddenName;

    public JpaForbiddenNameModel(String forbiddenName) {
        this.forbiddenName = forbiddenName;
    }

    protected JpaForbiddenNameModel() {}
}
