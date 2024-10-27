package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
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
 * @author  rmfranca*/

public class Lending {

    @Getter
    private String pk;

    private LendingNumber lendingNumber;

    /**
     * {@code Book} associated with this {@code Lending}.
     * */
    @Getter
    private Book book;

    /**
     * {@code Reader} associated with this {@code Lending}.
     **/
    @Getter
    private ReaderDetails readerDetails;

    /**
     * Date of this {@code Lending}'s creation.
     * */
    @Getter
    private LocalDate startDate;

    /**
     * Date this {@code Lending} is to be returned.
     * */
    @Getter
    private LocalDate limitDate;

    /**
     * Date this {@code Lending} is actually returned. This field is initialized as {@code null}
     * as the lending can never begin with the book already returned. The {@code null} value is used to
     * check if a book has been returned.
     * */
    @Getter
    private LocalDate returnedDate;

    // optimistic-lock
    /**
     * Version of the object, to handle concurrency of accesses.
     * */
    @Getter
    private long version;

    /**
     * Optional commentary written by a reader when the book is returned.
     * This field is initialized as null as the lending can never begin with the book already returned
     * */
    private String commentary = null;

    private Integer daysUntilReturn;

    private Integer daysOverdue;

    @Getter
    private int fineValuePerDayInCents;


    /**
     * Constructs a new {@code Lending} object to be persisted in the database.
     * <p>
     * Sets {@code startDate} as the current date, and {@code limitDate} as the current date plus the
     * business specified number of days a reader can take to return the book.
     *
     * @param       book {@code Book} object, which should be retrieved from the database.
     * @param       readerDetails {@code Reader} object, which should be retrieved from the database.
     * @param       seq sequential number, which should be obtained from the year's count on the database.
     * @throws      NullPointerException if any of the arguments is {@code null}
     * */
    public Lending(String id, Book book, ReaderDetails readerDetails, int seq, int lendingDuration, int fineValuePerDayInCents){
        try {
            this.book = Objects.requireNonNull(book);
            this.readerDetails = Objects.requireNonNull(readerDetails);
        }catch (NullPointerException e){
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        this.pk = id;
        this.lendingNumber = new LendingNumber(seq);
        this.startDate = LocalDate.now();
        this.limitDate = LocalDate.now().plusDays(lendingDuration);
        this.returnedDate = null;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        setDaysUntilReturn();
        setDaysOverdue();
    }

    /**
     * <p>Sets {@code commentary} and the current date as {@code returnedDate}.
     * <p>If {@code returnedDate} is after {@code limitDate}, fine is applied with corresponding value.
     *
     * @param       desiredVersion to prevent editing a stale object.
     * @param       commentary written by a reader.
     * @throws      StaleObjectStateException if object was already modified by another user.
     * @throws      IllegalArgumentException  if {@code returnedDate} already has a value.
     */
    public void setReturned(final long desiredVersion, final String commentary){

        if (this.returnedDate != null)
            throw new IllegalArgumentException("book has already been returned!");

        // check current version
        if (this.version != desiredVersion)
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        if(commentary != null)
            this.commentary = commentary;

        this.returnedDate = LocalDate.now();
    }

    /**
     * <p>Returns the number of days that the lending is/was past its due date</p>
     * @return      If the book was returned on time, or there is still time for it be returned, returns 0.
     * If the book has been returned with delay, returns the number of days of delay.
     * If the book has not been returned, returns the number of days
     * past its limit date.
     */
    public int getDaysDelayed(){
        if(this.returnedDate != null) {
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, this.returnedDate), 0);
        }else{
            return Math.max((int) ChronoUnit.DAYS.between(this.limitDate, LocalDate.now()), 0);
        }
    }

    private void setDaysUntilReturn(){
        int daysUntilReturn = (int) ChronoUnit.DAYS.between(LocalDate.now(), this.limitDate);
        if(this.returnedDate != null || daysUntilReturn < 0){
            this.daysUntilReturn = null;
        }else{
            this.daysUntilReturn = daysUntilReturn;
        }
    }

    private void setDaysOverdue(){
        int days = getDaysDelayed();
        if(days > 0){
            this.daysOverdue = days;
        }else{
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

    public Optional<Integer> getFineValueInCents() {
        Optional<Integer> fineValueInCents = Optional.empty();
        int days = getDaysDelayed();
        if(days > 0){
            fineValueInCents = Optional.of(fineValuePerDayInCents * days);
        }
        return fineValueInCents;
    }

    public String getTitle(){
        return this.book.getTitle().toString();
    }

    public String getLendingNumber() {
        return this.lendingNumber.toString();
    }


    /**Protected empty constructor for ORM only.*/
    protected Lending() {}

    /**Factory method meant to be only used in bootstrapping.*/
    public static Lending newBootstrappingLending(String id, Book book,
                                    ReaderDetails readerDetails,
                                    int year,
                                    int seq,
                                    LocalDate startDate,
                                    LocalDate returnedDate,
                                    int lendingDuration,
                                    int fineValuePerDayInCents){
        Lending lending = new Lending();

        try {
            lending.book = Objects.requireNonNull(book);
            lending.readerDetails = Objects.requireNonNull(readerDetails);
        }catch (NullPointerException e){
            throw new IllegalArgumentException("Null objects passed to lending");
        }
        lending.pk = id;
        lending.lendingNumber = new LendingNumber(year, seq);
        lending.startDate = startDate;
        lending.limitDate = startDate.plusDays(lendingDuration);
        lending.fineValuePerDayInCents = fineValuePerDayInCents;
        lending.returnedDate = returnedDate;
        return lending;

    }
}
