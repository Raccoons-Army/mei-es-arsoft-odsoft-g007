package pt.psoft.g1.psoftg1.genremanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "TopGenre")
public class JpaTopGenreModel {
    @Transient
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @Size(min = 1, max = GENRE_MAX_LENGTH, message = "Genre name must be between 1 and 100 characters")
    @Column(unique=true, nullable=false, length = GENRE_MAX_LENGTH)
    String genre;

    long lendingCount;

    // Empty constructor for JPA
    protected JpaTopGenreModel(){}

    public JpaTopGenreModel(String genre, long lendingCount) {
        this.genre = genre;
        this.lendingCount = lendingCount;
    }

}
