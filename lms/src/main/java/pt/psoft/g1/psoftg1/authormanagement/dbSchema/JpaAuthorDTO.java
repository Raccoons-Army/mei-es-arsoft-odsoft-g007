package pt.psoft.g1.psoftg1.authormanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
public class JpaAuthorDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AUTHOR_NUMBER")
    @Getter
    private Long authorNumber;

    @Version
    private long version;

    @NotNull
    @NotBlank
    @Column(name="NAME", length = 150)
    @Getter
    private String name;

    @Column(nullable = false, length = 4096)
    @NotNull
    @Size(min = 1, max = 4096)
    @Getter
    private String bio;

    public JpaAuthorDTO(Long authorNumber, long version, String name, String bio) {
        this.authorNumber = authorNumber;
        this.version = version;
        this.name = name;
        this.bio = bio;
    }

    public JpaAuthorDTO() {
    }
}
