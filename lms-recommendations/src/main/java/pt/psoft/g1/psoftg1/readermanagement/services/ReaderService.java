package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    void create(ReaderViewAMQP request);
}
