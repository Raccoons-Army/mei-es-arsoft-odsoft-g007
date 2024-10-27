package pt.psoft.g1.psoftg1.lendingmanagement.dbSchema;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "Fines")
public class MongoFineModel {
    @Id
    private String pk;

    private int fineValuePerDayInCents;

    private BigDecimal centsValue;

    @DBRef(lazy = true)
    private MongoLendingModel lending;

    @LastModifiedDate
    private LocalDate createdAt;

    public MongoFineModel(MongoLendingModel lending, int centsValue) {
        this.fineValuePerDayInCents = lending.getFineValuePerDayInCents();
        this.centsValue = BigDecimal.valueOf(centsValue);
        this.lending = lending;
    }

    protected MongoFineModel() {
    }
}
