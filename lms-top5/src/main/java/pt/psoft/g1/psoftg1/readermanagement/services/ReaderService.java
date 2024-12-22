package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    ReaderDetails create(ReaderViewAMQP readerViewAMQP);

    void delete(ReaderViewAMQP readerViewAMQP);

    Optional<ReaderDetails> findByUsername(final String username);

    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
}
