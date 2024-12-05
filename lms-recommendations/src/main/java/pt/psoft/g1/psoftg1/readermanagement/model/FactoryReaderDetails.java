package pt.psoft.g1.psoftg1.readermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class FactoryReaderDetails {

    public FactoryReaderDetails() {
    }

    public ReaderDetails newReaderDetails(String readerNumber) {
        return new ReaderDetails(readerNumber);
    }
}
