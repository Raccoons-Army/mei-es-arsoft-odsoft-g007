package pt.psoft.g1.psoftg1.publishers;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

public interface ReaderEventsPublisher {

    void sendReaderCreated(ReaderDetails readerDetails);

    void sendReaderUpdated(ReaderDetails readerDetails, Long currentVersion);

    void sendReaderDeleted(ReaderDetails readerDetails, Long currentVersion);
}
