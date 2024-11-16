package pt.psoft.g1.psoftg1.lendingmanagement.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.model.FactoryBook;
import pt.psoft.g1.psoftg1.readermanagement.model.FactoryReaderDetails;

@Component
public class FactoryLending {

    FactoryBook _factoryBook;
    FactoryReaderDetails _factoryReaderDetails;

    public FactoryLending(FactoryBook factoryBook, FactoryReaderDetails factoryReaderDetails) {
        _factoryBook = factoryBook;
        _factoryReaderDetails = factoryReaderDetails;
    }

    public Lending newLending(String id, int seq, int lendingDuration, int fineValuePerDayInCents) {
        return new Lending(id, seq, lendingDuration, fineValuePerDayInCents, _factoryBook, _factoryReaderDetails);
    }

}
