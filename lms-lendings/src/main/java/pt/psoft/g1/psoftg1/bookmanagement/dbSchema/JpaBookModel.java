package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Book")
public class JpaBookModel {

    @Id
    @Size(min = 10, max = 13)
    @Column(name = "ISBN", length = 16)
    private String isbn;

    @Version
    private Long version;

    public JpaBookModel(String isbn) {
        this.isbn = isbn;
    }

    // default constructor for JPA
    protected JpaBookModel() {
    }
}
