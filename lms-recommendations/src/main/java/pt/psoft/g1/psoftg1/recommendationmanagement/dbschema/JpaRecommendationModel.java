package pt.psoft.g1.psoftg1.recommendationmanagement.dbschema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Recommendation")
public class JpaRecommendationModel {

    @Id
    private String pk;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private JpaBookModel book;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private JpaReaderDetailsModel readerDetails;

    @NotNull
    private boolean positive;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @Getter
    private LocalDate createdAt;

    @Version
    @Getter
    private long version;

    public JpaRecommendationModel(JpaBookModel book, JpaReaderDetailsModel readerDetails, boolean positive) {

        this.book = book;
        this.readerDetails = readerDetails;
        this.createdAt = LocalDate.now();
        this.positive = positive;
    }

    /**
     * Protected empty constructor for ORM only.
     */
    protected JpaRecommendationModel() {
    }

}