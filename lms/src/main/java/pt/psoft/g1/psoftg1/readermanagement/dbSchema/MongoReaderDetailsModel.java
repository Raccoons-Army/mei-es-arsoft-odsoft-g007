package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoEntityWithPhotoModel;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "READER_DETAILS")
public class MongoReaderDetailsModel extends MongoEntityWithPhotoModel {
    @Id
    private String pk;

    @DBRef
    private MongoBookModel book;

    @DBRef
    private MongoReaderDetailsModel readerDetails;

    private String lendingNumber;
    private LocalDate startDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;
    private Integer fineValuePerDayInCents;
    private String commentary;
    private Long version;

    public MongoReaderDetailsModel(String pk, MongoBookModel book, MongoReaderDetailsModel readerDetails,
                             String lendingNumber, LocalDate startDate, LocalDate limitDate, LocalDate returnedDate,
                             Integer fineValuePerDayInCents, String commentary) {
        this.pk = pk;
        this.book = book;
        this.readerDetails = readerDetails;
        this.lendingNumber = lendingNumber;
        this.startDate = startDate;
        this.limitDate = limitDate;
        this.returnedDate = returnedDate;
        this.fineValuePerDayInCents = fineValuePerDayInCents;
        this.commentary = commentary;
    }

    protected MongoReaderDetailsModel() {
    }
}
