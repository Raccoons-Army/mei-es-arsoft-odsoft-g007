package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDetails extends EntityWithPhoto {

    @Setter
    @Getter
    private String pk;

    @Getter
    @Setter
    private String username;

    private ReaderNumber readerNumber;

    @Getter
    private BirthDate birthDate;

    private PhoneNumber phoneNumber;

    @Setter
    @Getter
    private boolean gdprConsent;

    @Setter
    @Getter
    private boolean marketingConsent;

    @Setter
    @Getter
    private boolean thirdPartySharingConsent;

    @Getter
    @Setter
    private Long version;

    @Getter
    @Setter
    private List<String> interestList = new ArrayList<>();

    public ReaderDetails(String readerNumber, String birthDate, String phoneNumber, boolean gdprConsent, boolean marketingConsent,
                         boolean thirdPartySharingConsent, String photoURI, String username, List<String> interestList) {
        if(phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }

        if(!gdprConsent) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        setReaderNumber(new ReaderNumber(readerNumber));
        setPhoneNumber(new PhoneNumber(phoneNumber));
        setBirthDate(new BirthDate(birthDate));
        //By the client specifications, gdpr can only have the value of true. A setter will be created anyways in case we have accept no gdpr consent later on the project
        setGdprConsent(true);

        setPhotoInternal(photoURI);
        setMarketingConsent(marketingConsent);
        setThirdPartySharingConsent(thirdPartySharingConsent);
        setUsername(username);
        setInterestList(interestList);
    }

    private void setPhoneNumber(PhoneNumber number) {
        if(number != null) {
            this.phoneNumber = number;
        }
    }

    private void setReaderNumber(ReaderNumber readerNumber) {
        if(readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    private void setBirthDate(BirthDate date) {
        if(date != null) {
            this.birthDate = date;
        }
    }

    public void applyPatch(final long currentVersion, final String birthDate, final String phoneNumber,
                           final boolean marketing, final boolean thirdParty, String photoURI, List<String> interestList) {

        if(currentVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        if(birthDate != null) {
            setBirthDate(new BirthDate(birthDate));
        }

        if(phoneNumber != null) {
            setPhoneNumber(new PhoneNumber(phoneNumber));
        }

        if(marketing != this.marketingConsent) {
            setMarketingConsent(marketing);
        }

        if(thirdParty != this.thirdPartySharingConsent) {
            setThirdPartySharingConsent(thirdParty);
        }

        if(photoURI != null) {
            try {
                setPhotoInternal(photoURI);
            } catch(InvalidPathException ignored) {}
        }

        if(interestList != null) {
            this.interestList = interestList;
        }
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }

    public String getReaderNumber(){
        return this.readerNumber.toString();
    }

    public String getPhoneNumber() { return this.phoneNumber.toString();}

    protected ReaderDetails() {
        // for ORM only
    }
}
