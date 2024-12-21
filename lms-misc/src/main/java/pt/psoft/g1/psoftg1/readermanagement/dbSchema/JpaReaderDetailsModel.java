package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@Table(name = "READER_DETAILS")
public class JpaReaderDetailsModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    @Column(name = "READER_USER_ID")
    private String username;

    @Column(name = "READER_NUMBER")
    private String readerNumber;

    @Version
    private Long version;


    public JpaReaderDetailsModel(String readerNumber, String username) {
        setUsername(username);
        setReaderNumber(readerNumber);
    }

    protected JpaReaderDetailsModel() {
        // for ORM only
    }
}
