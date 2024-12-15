package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "READER_DETAILS")
public class JpaReaderDetailsModel {

    @Id
    @Column(name = "READER_NUMBER", unique = true)
    private String readerNumber;

    @Version
    private Long version;

    public JpaReaderDetailsModel(String readerNumber) {
        setReaderNumber(readerNumber);
    }

    protected JpaReaderDetailsModel() {
        // for ORM only
    }
}
