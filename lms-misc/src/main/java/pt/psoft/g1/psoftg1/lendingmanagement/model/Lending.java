package pt.psoft.g1.psoftg1.lendingmanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@code Lending} class associates a {@code Reader} and a {@code Book}.
 * <p>It stores the date it was registered, the date it is supposed to
 * be returned, and the date it actually was returned.
 * It also stores an optional reader {@code commentary} (submitted at the time of the return) and
 * the {@code Fine}, if applicable.
 * <p>It is identified in the system by an auto-generated {@code id}, and has a unique-constrained
 * natural key ({@code LendingNumber}) with its own business rules.
 *
 * @author rmfranca
 */

public class Lending {

    @Getter
    @Setter
    private String pk;

    @Setter
    private LendingNumber lendingNumber;

    /**
     * {@code Book} associated with this {@code Lending}.
     */
    @Getter
    @Setter
    private Book book;

    /**
     * {@code Reader} associated with this {@code Lending}.
     **/
    @Getter
    @Setter
    private ReaderDetails readerDetails;

    /**
     * Date of this {@code Lending}'s creation.
     */
    @Getter
    @Setter
    private LocalDate startDate;

    /**
     * Date this {@code Lending} is to be returned.
     */
    @Getter
    @Setter
    private LocalDate limitDate;

    /**
     * Date this {@code Lending} is actually returned. This field is initialized as {@code null}
     * as the lending can never begin with the book already returned. The {@code null} value is used to
     * check if a book has been returned.
     */
    @Getter
    @Setter
    private LocalDate returnedDate;

    // optimistic-lock
    /**
     * Version of the object, to handle concurrency of accesses.
     */
    @Getter
    @Setter
    private long version;

    private Integer daysUntilReturn;

    private Integer daysOverdue;

    private FactoryBook _factoryBook;
    private FactoryReaderDetails _factoryReaderDetails;


    public Lending(String id, Book book, ReaderDetails readerDetails, int seq, int lendingDuration) {
        try {
            this.book = Objects.requireNonNull(book);
            this.readerDetails = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.pk = id;
        this.lendingNumber = new LendingNumber(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        setDaysUntilReturn();
        setDaysOverdue();
    }

    public Lending(String id, Book book, ReaderDetails readerDetails, String lendingNumber, LocalDate startDate,
                   LocalDate limitDate) {
        try {
            this.book = Objects.requireNonNull(book);
            this.readerDetails = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.pk = id;
        this.lendingNumber = new LendingNumber(lendingNumber);
        this.startDate = startDate;
        this.limitDate = limitDate;
        this.returnedDate = null;
        setDaysUntilReturn();
        setDaysOverdue();
    }

    public Lending(String id, int seq, int lendingDuration, FactoryBook factoryBook, FactoryReaderDetails factoryReaderDetails) {
        this.pk = id;
        this.lendingNumber = new LendingNumber(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        setDaysUntilReturn();
        setDaysOverdue();
        _factoryBook = factoryBook;
        _factoryReaderDetails = factoryReaderDetails;
    }

    public Book defineBook(String isbn, String title, String description) {
        this.book = _factoryBook.newBook(isbn, title, description);
        return this.book;
    }

    public ReaderDetails defineReaderDetails(String readerNumber, String username) {
        this.readerDetails = _factoryReaderDetails.newReaderDetails(readerNumber, username);
        return this.readerDetails;
    }

    /**
     * <p>Sets {@code commentary} and the current date as {@code returnedDate}.
     * <p>If {@code returnedDate} is after {@code limitDate}, fine is applied with corresponding value.
     *
     * @param desiredVersion to prevent editing a stale object.
     * @throws StaleObjectStateException if object was already modified by another user.
     * @throws IllegalArgumentException  if {@code returnedDate} already has a value.
     */
    public void setReturned(final long desiredVersion) {

        if (this.returnedDate != null)
            throw new IllegalArgumentException("book has already been returned!");

        // check current version
        if (this.version != desiredVersion)
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        this.returnedDate = LocalDate.now();
    }

    /**
     * <p>Returns the number of days that the lending is/was past its due date</p>
     *
     * @return If the book was returned on time, or there is still time for it be returned, returns 0.
     * If the book has been returned with delay, returns the number of days of delay.
     * If the book has not been returned, returns the number of days
     * past its limit date.
     */
    public int getDaysDelayed() {
        if (this.returnedDate != null) {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, this.returnedDate), 0);
        } else {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, LocalDate.now()), 0);
        }
    }

    private void setDaysUntilReturn() {
        int daysUntilReturn = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.limitDate);
        if (this.returnedDate != null || daysUntilReturn < 0) {
            this.daysUntilReturn = null;
        } else {
            this.daysUntilReturn = daysUntilReturn;
        }
    }

    private void setDaysOverdue() {
        int days = getDaysDelayed();
        if (days > 0) {
            this.daysOverdue = days;
        } else {
            this.daysOverdue = null;
        }
    }

    public Optional<Integer> getDaysUntilReturn() {
        setDaysUntilReturn();
        return Optional.ofNullable(daysUntilReturn);
    }

    public Optional<Integer> getDaysOverdue() {
        setDaysOverdue();
        return Optional.ofNullable(daysOverdue);
    }

    public String getLendingNumber() {
        return this.lendingNumber.toString();
    }


    // for mapper
    public Lending() {
    }

    /**
     * Factory method meant to be only used in bootstrapping.
     */
    public static Lending newBootstrappingLending(String id, Book book,
                                                  ReaderDetails readerDetails,
                                                  int year,
                                                  int seq,
                                                  LocalDate startDate,
                                                  LocalDate returnedDate,
                                                  int lendingDuration) {
        Lending lending = new Lending();

        try {
            lending.book = Objects.requireNonNull(book);
            lending.readerDetails = Objects.requireNonNull(readerDetails);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        lending.pk = id;
        lending.lendingNumber = new LendingNumber(year, seq);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.returnedDate = returnedDate;
        return lending;

    }

}
