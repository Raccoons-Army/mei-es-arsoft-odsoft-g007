package pt.psoft.g1.psoftg1.publishers;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

public interface ReaderEventsPublisher {

    ReaderViewAMQP sendReaderCreated(ReaderDetails readerDetails);

    ReaderViewAMQP sendReaderUpdated(ReaderDetails readerDetails, Long currentVersion);

    ReaderViewAMQP sendReaderDeleted(ReaderDetails readerDetails, Long currentVersion);
}
