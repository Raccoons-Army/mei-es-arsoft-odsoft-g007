package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
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
    @Setter
    @Getter
    private long pk;

    @Version
    @Setter
    @Getter
    private Long version;

    @Size(min = 10, max = 13)
    @Column(name="ISBN", length = 16)
    @Setter
    @Getter
    private String isbn;

    @Size(max = 128)
    @Column(name = "TITLE", length = 128)
    @Setter
    @Getter
    private String title;

    @ManyToOne
    @Setter
    @Getter
    private Genre genre;

    @ManyToMany
    @Setter
    @Getter
    private List<Author> authors = new ArrayList<>();

    @Size(max = 4096)
    @Column(length = 4096)
    @Setter
    @Getter
    private String description;

    // default constructor for JPA
    protected JpaBookDTO() {}
}
