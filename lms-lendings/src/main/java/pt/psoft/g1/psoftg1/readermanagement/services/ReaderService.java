package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    void create(CreateReaderRequest request);
    Iterable<ReaderDetails> findAll();
    Optional<ReaderDetails> findByUsername(final String username);
}
