package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

public class ReaderDetails {

    @Setter
    @Getter
    private String pk;

    @Getter
    @Setter
    private String username;

    private ReaderNumber readerNumber;

    @Getter
    @Setter
    private Long version;

    public ReaderDetails(String readerNumber, String username) {
        setReaderNumber(new ReaderNumber(readerNumber));
        setUsername(username);
    }

    private void setReaderNumber(ReaderNumber readerNumber) {
        if(readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    public String getReaderNumber(){
        return this.readerNumber.toString();
    }

    protected ReaderDetails() {
        // for ORM only
    }
}
