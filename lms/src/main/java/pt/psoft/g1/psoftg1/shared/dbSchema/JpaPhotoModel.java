package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@Entity
public class JpaPhotoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pk;

    @NotNull
    private String photoFile;

    public JpaPhotoModel(String photoPath) {
        this.photoFile = photoPath;
    }

    protected JpaPhotoModel() {
    }
}
