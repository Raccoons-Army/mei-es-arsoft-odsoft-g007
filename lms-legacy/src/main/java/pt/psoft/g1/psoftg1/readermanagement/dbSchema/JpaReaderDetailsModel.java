package pt.psoft.g1.psoftg1.readermanagement.dbSchema;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaEntityWithPhotoModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "READER_DETAILS")
public class JpaReaderDetailsModel extends JpaEntityWithPhotoModel {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String pk;

    @OneToOne
    @JoinColumn(name = "READER_USER_ID")
    private JpaUserModel reader;

    @Column(name = "READER_NUMBER", unique = true)
    private String readerNumber;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    private String phoneNumber;

    @Basic
    private boolean gdprConsent;

    @Basic
    private boolean marketingConsent;

    @Basic
    private boolean thirdPartySharingConsent;

    @Version
    private Long version;

    @ManyToMany
    private List<JpaGenreModel> interestList;

    public JpaReaderDetailsModel(String readerNumber, JpaReaderModel reader, LocalDate birthDate, String phoneNumber,
                                 boolean gdprConsent, boolean marketingConsent, boolean thirdPartySharingConsent,
                                 String photoURI, List<JpaGenreModel> interestList) {
        setReader(reader);
        setReaderNumber(readerNumber);
        setPhoneNumber(phoneNumber);
        setBirthDate(birthDate);
        setGdprConsent(gdprConsent);
        setPhotoInternal(photoURI);
        setMarketingConsent(marketingConsent);
        setThirdPartySharingConsent(thirdPartySharingConsent);
        setInterestList(interestList);
    }

    protected JpaReaderDetailsModel() {
        // for ORM only
    }
}
