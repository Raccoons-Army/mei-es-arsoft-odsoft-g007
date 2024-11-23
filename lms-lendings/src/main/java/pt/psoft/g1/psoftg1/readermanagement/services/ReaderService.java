package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    void create(ReaderDetailsViewAMQP request);
    Optional<ReaderDetails> findByUsername(final String username);
}
