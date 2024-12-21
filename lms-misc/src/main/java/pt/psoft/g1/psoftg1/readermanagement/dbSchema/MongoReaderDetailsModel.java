package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "readersDetails")
public class MongoReaderDetailsModel {

    @Id
    private String pk;

    private String username;

    private String readerNumber;

    @Version
    private Long version;

    public MongoReaderDetailsModel(String readerNumber, String username) {
        this.username = username;
        this.readerNumber = readerNumber;
    }

    protected MongoReaderDetailsModel() {
    }
}
