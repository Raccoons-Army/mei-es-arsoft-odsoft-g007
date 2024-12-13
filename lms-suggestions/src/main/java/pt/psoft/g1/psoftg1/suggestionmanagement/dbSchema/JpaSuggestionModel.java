package pt.psoft.g1.psoftg1.suggestionmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Suggestion")
public class JpaSuggestionModel {

    @Id
    private String pk;

    @Size(min = 10, max = 13)
    @Column(name = "ISBN", length = 16)
    private String isbn;

    /**
     * Date of this {@code Suggestion}'s creation.
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    /**
     * {@code Reader} associated with this {@code Suggestion}.
     **/
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private JpaReaderDetailsModel readerDetails;

    @Version
    private long version;

    public JpaSuggestionModel(String isbn, LocalDate createdAt, JpaReaderDetailsModel readerDetails) {
        this.isbn = isbn;
        this.createdAt = createdAt;
        this.readerDetails = readerDetails;
    }

    /**
     * Protected empty constructor for ORM only.
     */
    protected JpaSuggestionModel() {}
}
