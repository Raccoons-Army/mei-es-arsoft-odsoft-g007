package pt.psoft.g1.psoftg1.readermanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;

@Component
public class FactoryReaderDetails {

    FactoryUser _factoryUser;
    FactoryGenre _factoryGenre;

    public FactoryReaderDetails(FactoryUser factoryUser, FactoryGenre factoryGenre) {
        _factoryUser = factoryUser;
        _factoryGenre = factoryGenre;
    }

    public ReaderDetails newReaderDetails(int readerNumber, String birthDate, String phoneNumber, boolean gdpr, boolean marketing,
                                          boolean thirdParty, String photoURI) {
        return new ReaderDetails(readerNumber, birthDate, phoneNumber, gdpr, marketing, thirdParty, photoURI, _factoryUser, _factoryGenre);
    }
}
