package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table
public class JpaGenreModel {
    @Transient
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    long id;

    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Column(unique=true, nullable=false, length = GENRE_MAX_LENGTH)
    @Getter
    String genre;

    // Empty constructor for JPA
    protected JpaGenreModel(){}

    public JpaGenreModel(long id, String genre) {
        this.id = id;
        this.genre = genre;
    }

    public JpaGenreModel(long id) {
        this.id = id;
    }

}
