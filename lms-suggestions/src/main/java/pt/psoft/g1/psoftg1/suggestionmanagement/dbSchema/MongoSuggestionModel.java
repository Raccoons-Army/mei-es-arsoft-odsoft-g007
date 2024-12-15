package pt.psoft.g1.psoftg1.suggestionmanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "suggestions")
public class MongoSuggestionModel {

    @Id
    private String pk;

    private String isbn;

    private LocalDate createdAt;

    @DBRef
    private MongoReaderDetailsModel readerDetails;

    private Long version;

    public MongoSuggestionModel(String pk, String isbn, MongoReaderDetailsModel readerDetails, LocalDate createdAt) {
        this.pk = pk;
        this.isbn = isbn;
        this.readerDetails = readerDetails;
        this.createdAt = createdAt;
    }

    protected MongoSuggestionModel() {}
}
