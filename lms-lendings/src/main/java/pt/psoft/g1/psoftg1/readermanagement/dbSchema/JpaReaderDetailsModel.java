package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;

@Getter
@Setter
@Entity
@Table(name = "READER_DETAILS")
public class JpaReaderDetailsModel {

    @Id
    @Column(name = "READER_NUMBER", unique = true)
    private String readerNumber;

    @OneToOne
    @JoinColumn(name = "READER_USER_ID")
    private JpaUserModel reader;

    @Version
    private Long version;

    public JpaReaderDetailsModel(String readerNumber, JpaReaderModel reader) {
        this.reader = reader;
        setReaderNumber(readerNumber);
    }

    protected JpaReaderDetailsModel() {
        // for ORM only
    }
}
