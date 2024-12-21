package pt.psoft.g1.psoftg1.lendingmanagement.services;

import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface LendingService {
    List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn, Optional<Boolean> returned);
    Lending create(LendingViewAMQP resource);
    Lending update(LendingViewAMQP resource);
    Double getAverageDuration();
    List<Lending> getOverdue(Page page);
    Double getAvgLendingDurationByIsbn(String isbn);
}
