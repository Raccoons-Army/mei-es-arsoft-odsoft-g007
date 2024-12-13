package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "readersDetails")
public class MongoReaderDetailsModel {

    @Id
    private String readerNumber;

    @Version
    private Long version;

    public MongoReaderDetailsModel(String readerNumber) {
        this.readerNumber = readerNumber;
    }

    protected MongoReaderDetailsModel() {
    }
}
