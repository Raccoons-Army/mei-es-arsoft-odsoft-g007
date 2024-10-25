package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.nio.file.InvalidPathException;
import java.util.List;

public class ReaderDetails extends EntityWithPhoto {

    @Getter
    private Long pk;

    @Getter
    @Setter
    private Reader reader;

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
    private Long version;

    @Getter
    @Setter
    private List<Genre> interestList;

    FactoryUser _factoryUser;
    FactoryGenre _factoryGenre;

    public ReaderDetails(int readerNumber, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, FactoryUser factoryUser, FactoryGenre factoryGenre) {
        if(reader == null || phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }

        if(!gdpr) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        setReaderNumber(new ReaderNumber(readerNumber));
        setPhoneNumber(new PhoneNumber(phoneNumber));
        setBirthDate(new BirthDate(birthDate));
        //By the client specifications, gdpr can only have the value of true. A setter will be created anyways in case we have accept no gdpr consent later on the project
        setGdprConsent(true);

        setPhotoInternal(photoURI);
        setMarketingConsent(marketing);
        setThirdPartySharingConsent(thirdParty);

        _factoryUser = factoryUser;
        _factoryGenre = factoryGenre;
    }

    public Reader defineReader(String username, String password, String name) {
        this.reader = _factoryUser.newReader(username, password, name);
        return this.reader;
    }

    public Reader defineReader(String username, String name) {
        this.reader = _factoryUser.newReader(username, name);
        return this.reader;
    }

    public Genre addGenre(String name) throws InstantiationException {
        Genre genre = _factoryGenre.newGenre(name);
        this.interestList.add(genre);
        return genre;
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

    public void applyPatch(final long currentVersion, final UpdateReaderRequest request, String photoURI, List<Genre> interestList) {
        if(currentVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        String birthDate = request.getBirthDate();
        String phoneNumber = request.getPhoneNumber();
        boolean marketing = request.getMarketing();
        boolean thirdParty = request.getThirdParty();
        String fullName = request.getFullName();
        String username = request.getUsername();
        String password = request.getPassword();

        if(username != null) {
            this.reader.setUsername(username);
        }

        if(password != null) {
            this.reader.setPassword(password);
        }

        if(fullName != null) {
            this.reader.setName(fullName);
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
