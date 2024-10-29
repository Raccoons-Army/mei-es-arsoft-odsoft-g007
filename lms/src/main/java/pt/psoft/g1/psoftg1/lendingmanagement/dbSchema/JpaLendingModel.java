package pt.psoft.g1.psoftg1.lendingmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Lending", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"LENDING_NUMBER"})})
public class JpaLendingModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    @Column(name = "LENDING_NUMBER", length = 32)
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String lendingNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private JpaBookModel book;


    /**
     * {@code Reader} associated with this {@code Lending}.
     **/
    @NotNull
    @Getter
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private JpaReaderDetailsModel readerDetails;

    /**
     * Date of this {@code Lending}'s creation.
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @Getter
    private LocalDate startDate;

    /**
     * Date this {@code Lending} is to be returned.
     */
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter
    private LocalDate limitDate;

    /**
     * Date this {@code Lending} is actually returned. This field is initialized as {@code null}
     * as the lending can never begin with the book already returned. The {@code null} value is used to
     * check if a book has been returned.
     */
    @Temporal(TemporalType.DATE)
    @Getter
    private LocalDate returnedDate;

    // optimistic-lock
    /**
     * Version of the object, to handle concurrency of accesses.
     */
    @Version
    @Getter
    private long version;

    /**
     * Optional commentary written by a reader when the book is returned.
     * This field is initialized as null as the lending can never begin with the book already returned
     */
    @Size(min = 0, max = 1024)
    @Column(length = 1024)
    private String commentary = null;

    @Getter
    private int fineValuePerDayInCents;


    public JpaLendingModel(JpaBookModel book, JpaReaderDetailsModel readerDetails, String lendingNumber,
                           int fineValuePerDayInCents, LocalDate startDate, LocalDate limitDate, LocalDate returnedDate,
                           String commentary) {

        this.book = book;
        this.readerDetails = readerDetails;
        this.lendingNumber = lendingNumber;
        this.returnedDate = returnedDate;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        this.limitDate = limitDate;
        this.startDate = startDate;
        this.commentary = commentary;
    }

    /**
     * Protected empty constructor for ORM only.
     */
    protected JpaLendingModel() {
    }

}