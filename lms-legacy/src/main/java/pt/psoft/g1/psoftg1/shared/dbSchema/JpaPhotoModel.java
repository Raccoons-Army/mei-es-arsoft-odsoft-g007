package pt.psoft.g1.psoftg1.shared.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.nio.file.Path;

@Getter
@Setter
@Entity
@Table(name = "PHOTO")
public class JpaPhotoModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    @NotNull
    private String photoFile;

    public JpaPhotoModel(String photoPath) {
        this.photoFile = photoPath;
    }

    protected JpaPhotoModel() {
    }
}
