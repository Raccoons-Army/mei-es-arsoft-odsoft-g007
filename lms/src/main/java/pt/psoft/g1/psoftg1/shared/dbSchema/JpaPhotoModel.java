package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Entity
public class JpaPhotoModel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long pk;

    @Getter
    @Setter
    @NotNull
    private String photoFile;

    public JpaPhotoModel(Path photoPath) {
        setPhotoFile(photoPath.toString());
    }

    protected JpaPhotoModel() {}
}
