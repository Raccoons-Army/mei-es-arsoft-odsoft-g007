package pt.psoft.g1.psoftg1.lendingmanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LendingRepository extends CRUDRepository<Lending, Long> {
    Optional<Lending> findByLendingNumber(String lendingNumber);
}
