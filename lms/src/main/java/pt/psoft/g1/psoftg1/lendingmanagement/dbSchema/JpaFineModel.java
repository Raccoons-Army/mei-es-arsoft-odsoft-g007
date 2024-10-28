package pt.psoft.g1.psoftg1.lendingmanagement.dbSchema;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Fine")
public class JpaFineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk;

    @PositiveOrZero
    @Column(updatable = false)
    private int fineValuePerDayInCents;

    /**Fine value in Euro cents*/
    @PositiveOrZero
    int centsValue;

    @Setter
    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "lending_pk", nullable = false, unique = true)
    private JpaLendingModel lending;

    /**
     * Constructs a new {@code Fine} object. Sets the current value of the fine,
     * as well as the fine value per day at the time of creation.
     * @param   lending transaction which generates this fine.
     * */
    public JpaFineModel(JpaLendingModel lending, int centsValue) {
        this.fineValuePerDayInCents = lending.getFineValuePerDayInCents();
        this.centsValue = centsValue;
        this.lending = Objects.requireNonNull(lending);
    }

    /**Protected empty constructor for ORM only.*/
    protected JpaFineModel() {}
}
