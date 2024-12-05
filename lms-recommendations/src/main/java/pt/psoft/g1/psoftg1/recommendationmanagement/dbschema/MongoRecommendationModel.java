package pt.psoft.g1.psoftg1.recommendationmanagement.dbschema;

import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "Recommendations")
public class MongoRecommendationModel {
    @Id
    private String pk;

    @DBRef
    private MongoBookModel book;

    @DBRef
    private MongoReaderDetailsModel readerDetails;

    @LastModifiedDate
    private LocalDate createdAt;

    private boolean positive;

    @Version
    private Long version;

    public MongoRecommendationModel(MongoBookModel book, MongoReaderDetailsModel readerDetails, boolean positive) {
        this.book = book;
        this.readerDetails = readerDetails;
        this.positive = positive;
    }

    protected MongoRecommendationModel() {
    }
}
