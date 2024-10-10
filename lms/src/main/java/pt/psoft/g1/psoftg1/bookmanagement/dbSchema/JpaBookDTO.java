package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
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

    @Embedded
    @Setter
    @Getter
    private Isbn isbn;

    @Embedded
    @Setter
    @Getter
    private Title title;

    @ManyToOne
    @Setter
    @Getter
    private Genre genre;

    @ManyToMany
    @Setter
    @Getter
    private List<Author> authors = new ArrayList<>();

    @Embedded
    @Setter
    @Getter
    private Description description;

    // default constructor for JPA
    protected JpaBookDTO() {}
}
