package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class JpaGenreModel {
    @Transient
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long pk;

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Column(unique=true, nullable=false, length = GENRE_MAX_LENGTH)
    String genre;

    // Empty constructor for JPA
    protected JpaGenreModel(){}

    public JpaGenreModel(long pk, String genre) {
        this.pk = pk;
        this.genre = genre;
    }

}
