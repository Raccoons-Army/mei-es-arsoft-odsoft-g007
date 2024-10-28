package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaEntityWithPhotoModel;

@Getter
@Setter
@Entity
@Table(name = "Author")
public class JpaAuthorModel extends JpaEntityWithPhotoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUTHOR_NUMBER")
    private Long authorNumber;

    @Version
    private long version;

    @NotNull
    @NotBlank
    @Column(name="NAME", length = 150)
    private String name;

    @Column(nullable = false, length = 4096)
    @NotNull
    @Size(min = 1, max = 4096)
    private String bio;

    protected JpaAuthorModel() {
    }

    public JpaAuthorModel(Long authorNumber, long version, String name, String bio, String photoURI) {
        this.authorNumber = authorNumber;
        this.version = version;
        this.name = name;
        this.bio = bio;
        setPhotoInternal(photoURI);
    }
}
