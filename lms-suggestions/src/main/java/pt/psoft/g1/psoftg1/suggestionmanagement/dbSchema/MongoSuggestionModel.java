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

    private String suggestedBook;

    private LocalDate suggestionDate;

    @DBRef
    private MongoReaderDetailsModel readerDetails;

    private Long version;

    public MongoSuggestionModel(String pk, String suggestedBook, MongoReaderDetailsModel readerDetails, LocalDate suggestionDate) {
        this.pk = pk;
        this.suggestedBook = suggestedBook;
        this.readerDetails = readerDetails;
        this.suggestionDate = suggestionDate;
    }

    protected MongoSuggestionModel() {}
}
