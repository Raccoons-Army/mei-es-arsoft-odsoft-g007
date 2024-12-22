package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Book", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
public class JpaBookModel {

    @Version
    private Long version;

    @Id
    @Size(min = 10, max = 13)
    @Column(name = "ISBN", length = 16)
    private String isbn;

    @Size(max = 128)
    @Column(name = "TITLE", length = 128)
    private String title;

    @ManyToOne
    private JpaGenreModel genre;

    @ManyToMany
    private List<JpaAuthorModel> authors = new ArrayList<>();

    @Size(max = 4096)
    @Column(length = 4096)
    private String description;

    public JpaBookModel(Long version, String isbn, String title, JpaGenreModel genre,
                        List<JpaAuthorModel> authors, String description) {
        this.version = version;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
    }

    // default constructor for JPA
    protected JpaBookModel() {
    }
}
