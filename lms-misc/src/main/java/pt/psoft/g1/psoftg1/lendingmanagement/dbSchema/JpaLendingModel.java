package pt.psoft.g1.psoftg1.lendingmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
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


    public JpaLendingModel(JpaBookModel book, JpaReaderDetailsModel readerDetails, String lendingNumber,
                           LocalDate startDate, LocalDate limitDate, LocalDate returnedDate) {

        this.book = book;
        this.readerDetails = readerDetails;
        this.lendingNumber = lendingNumber;
        this.returnedDate = returnedDate;
        this.limitDate = limitDate;
        this.startDate = startDate;
    }

    /**
     * Protected empty constructor for ORM only.
     */
    protected JpaLendingModel() {
    }

}