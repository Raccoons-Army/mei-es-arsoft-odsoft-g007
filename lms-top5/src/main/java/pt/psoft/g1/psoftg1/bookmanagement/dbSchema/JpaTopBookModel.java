package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TopBook", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
public class JpaTopBookModel {

    @Version
    private Long version;

    @Id
    @Size(min = 10, max = 13)
    @Column(name = "ISBN", length = 16)
    private String isbn;

    @Size(max = 128)
    @Column(name = "TITLE", length = 128)
    private String title;

    private String genre;

    @ElementCollection
    @CollectionTable(name = "top_book_authors", joinColumns = @JoinColumn(name = "top_book_isbn"))
    @Column(name = "author_name")
    private List<String> authors = new ArrayList<>();

    @Size(max = 4096)
    @Column(length = 4096)
    private String description;

    private long lendingCount;

    public JpaTopBookModel(Long version, String isbn, String title, String genre,
                           List<String> authors, String description, long lendingCount) {
        this.version = version;
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.description = description;
        this.lendingCount = lendingCount;
    }

    // default constructor for JPA
    protected JpaTopBookModel() {
    }
}
