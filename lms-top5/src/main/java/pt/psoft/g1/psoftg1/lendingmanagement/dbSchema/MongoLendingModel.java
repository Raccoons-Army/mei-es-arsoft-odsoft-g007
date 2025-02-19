package pt.psoft.g1.psoftg1.lendingmanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "lendings")
public class MongoLendingModel {

    @Id
    private String pk;

    @DBRef
    private MongoBookModel book;

    private String lendingNumber;
    private LocalDate startDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;

    private Long version;

    public MongoLendingModel(String pk, MongoBookModel book,
                             String lendingNumber, LocalDate startDate, LocalDate limitDate, LocalDate returnedDate) {
        this.pk = pk;
        this.book = book;
        this.lendingNumber = lendingNumber;
        this.startDate = startDate;
        this.limitDate = limitDate;
        this.returnedDate = returnedDate;
    }

    protected MongoLendingModel() {
    }
}
