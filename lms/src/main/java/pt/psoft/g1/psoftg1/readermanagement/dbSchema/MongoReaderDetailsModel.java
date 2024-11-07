package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoEntityWithPhotoModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Document(collection = "readersDetails")
public class MongoReaderDetailsModel extends MongoEntityWithPhotoModel {

    @Id
    private String pk;

    @DBRef
    private MongoUserModel reader;

    private String readerNumber;

    private LocalDate birthDate;

    private String phoneNumber;

    private boolean gdprConsent;

    private boolean marketingConsent;

    private boolean thirdPartySharingConsent;

    @Version
    private Long version;

    @DBRef
    private List<MongoGenreModel> interestList;

    public MongoReaderDetailsModel(String readerNumber, MongoReaderModel reader, LocalDate birthDate, String phoneNumber,
                                   boolean gdprConsent, boolean marketingConsent, boolean thirdPartySharingConsent, String photoURI,
                                   List<MongoGenreModel> interestList) {
        this.reader = reader;
        this.readerNumber = readerNumber;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gdprConsent = gdprConsent;
        setPhotoInternal(photoURI);
        this.marketingConsent = marketingConsent;
        this.thirdPartySharingConsent = thirdPartySharingConsent;
        this.interestList = interestList;
    }

    protected MongoReaderDetailsModel() {
    }
}
