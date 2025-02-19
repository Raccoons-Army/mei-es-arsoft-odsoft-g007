package pt.psoft.g1.psoftg1.lendingmanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.Objects;

/**
 * The {@code Fine} class models a fine applied when a lending is past its due date.
 * <p>It stores its current value, and the associated {@code Lending}.
 * @author  rmfranca*/
@Getter
public class Fine {

    @Setter
    private Long pk;

    @Setter
    private int fineValuePerDayInCents;

    @Setter
    int centsValue;

    @Setter
    private Lending lending;

    private FactoryLending _factoryLending;

    /**
     * Constructs a new {@code Fine} object. Sets the current value of the fine,
     * as well as the fine value per day at the time of creation.
     * @param   lending transaction which generates this fine.
     * */
    public Fine(Lending lending) {
        if(lending.getDaysDelayed() <= 0)
            throw new IllegalArgumentException("Lending is not overdue");
        fineValuePerDayInCents = lending.getFineValuePerDayInCents();
        centsValue = fineValuePerDayInCents * lending.getDaysDelayed();
        this.lending = Objects.requireNonNull(lending);
    }

    public Fine(FactoryLending factoryLending) {
        this._factoryLending = factoryLending;
    }

    public Lending defineLending(String id, int seq, int lendingDuration, int fineValuePerDayInCents) {
        this.lending = _factoryLending.newLending(id, seq, lendingDuration, fineValuePerDayInCents);
        return this.lending;
    }

    // for mapper
    public Fine() {}
}
