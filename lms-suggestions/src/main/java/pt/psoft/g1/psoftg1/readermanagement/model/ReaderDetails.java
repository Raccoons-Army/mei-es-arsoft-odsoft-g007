package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

@Getter
@Setter
public class ReaderDetails {

    private ReaderNumber readerNumber;

    private Reader reader;

    Long version;

    FactoryUser _factoryUser;

    public ReaderDetails(String readerNumber, FactoryUser factoryUser) {
        setReaderNumber(new ReaderNumber(readerNumber));
        _factoryUser = factoryUser;
    }

    private void setReaderNumber(ReaderNumber readerNumber) {
        if (readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    public String getReaderNumber() {
        return this.readerNumber.toString();
    }

    // for mapper
    public Reader defineReader(String pk, String username, long version) {
        this.reader = _factoryUser.newReader(pk, username, version);
        return this.reader;
    }

    public Reader defineReader(String username) {
        this.reader = _factoryUser.newReader(username);
        return this.reader;
    }

    protected ReaderDetails() {
        // for ORM only
    }
}
