package pt.psoft.g1.psoftg1.bookmanagement.dbSchema;

import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "books")
public class MongoBookModel {

    @Id
    private String isbn;

    @Version
    private Long version;

    public MongoBookModel(String isbn) {
        this.isbn = isbn;
    }

    protected MongoBookModel() {
    }
}
