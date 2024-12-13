package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class ReaderNumber implements Serializable {

    private String readerNumber;

    public ReaderNumber(int year, int number) {
        this.readerNumber = year + "/" + number;
    }

    public ReaderNumber(String number) {
        // if contains / already, then just set the value
        if (number.contains("/")) {
            this.readerNumber = number;
            return;
        }
        this.readerNumber = LocalDate.now().getYear() + "/" + number;
    }

    protected ReaderNumber() {}

    public String toString() {
        return this.readerNumber;
    }
}
