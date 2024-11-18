package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;

@Getter
@Setter
@Document(collection = "readersDetails")
public class MongoReaderDetailsModel {

    @Id
    private String readerNumber;

    @DBRef
    private MongoUserModel reader;

    @Version
    private Long version;

    public MongoReaderDetailsModel(String readerNumber, MongoReaderModel reader) {
        this.readerNumber = readerNumber;
        this.reader = reader;
    }

    protected MongoReaderDetailsModel() {
    }
}
