package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;

public class ReaderDetails {

    private ReaderNumber readerNumber;

    @Getter
    @Setter
    Long version;

    public ReaderDetails(String readerNumber) {
        setReaderNumber(new ReaderNumber(readerNumber));
    }

    private void setReaderNumber(ReaderNumber readerNumber) {
        if (readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    public String getReaderNumber() {
        return this.readerNumber.toString();
    }

    protected ReaderDetails() {
        // for ORM only
    }
}
