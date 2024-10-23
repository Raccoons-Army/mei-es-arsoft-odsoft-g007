package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "Photo")
public class JpaPhotoDTO {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @NotNull
    private String photoFile;

    public JpaPhotoDTO(long id, String photoFile) {
        this.id = id;
        this.photoFile = photoFile;
    }

    public JpaPhotoDTO() {}
}
