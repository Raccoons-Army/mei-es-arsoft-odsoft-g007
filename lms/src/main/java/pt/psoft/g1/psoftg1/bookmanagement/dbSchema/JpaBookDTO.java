package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorDTO;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Book", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
public class JpaBookDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private String id;

    @Version
    @Getter
    private Long version;

    @Size(min = 10, max = 13)
    @Column(name="ISBN", length = 16)
    @Getter
    private String isbn;

    @Size(max = 128)
    @Column(name = "TITLE", length = 128)
    @Getter
    private String title;

    @ManyToOne
    @Getter
    private JpaGenreDTO genre;

    @ManyToMany
    @Getter
    private List<JpaAuthorDTO> authors = new ArrayList<>();

    @Size(max = 4096)
    @Column(length = 4096)
    @Getter
    private String description;

    public JpaBookDTO(String id, Long version, String isbn, String title, JpaGenreDTO genre, List<JpaAuthorDTO> authors, String description) {
        this.id = id;
        this.version = version;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
    }

    // default constructor for JPA
    protected JpaBookDTO() {}
}
