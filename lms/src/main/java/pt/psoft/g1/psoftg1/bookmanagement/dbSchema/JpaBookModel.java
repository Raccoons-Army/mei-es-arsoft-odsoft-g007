package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Book", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
public class JpaBookModel {

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
    private JpaGenreModel genre;

    @ManyToMany
    @Getter
    private List<JpaAuthorModel> authors = new ArrayList<>();

    @Size(max = 4096)
    @Column(length = 4096)
    @Getter
    private String description;

    public JpaBookModel(String id, Long version, String isbn, String title, JpaGenreModel genre, List<JpaAuthorModel> authors, String description) {
        this.id = id;
        this.version = version;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
    }

    // default constructor for JPA
    protected JpaBookModel() {}
}
