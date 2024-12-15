package pt.psoft.g1.psoftg1.suggestedbookmanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "suggestedBooks")
public class MongoSuggestedBookModel {

    @Id
    private String pk;

    @Version
    private Long version;

    private String isbn;

    public MongoSuggestedBookModel(String pk, Long version, String isbn) {
        this.pk = pk;
        this.version = version;
        this.isbn = isbn;
    }
}
