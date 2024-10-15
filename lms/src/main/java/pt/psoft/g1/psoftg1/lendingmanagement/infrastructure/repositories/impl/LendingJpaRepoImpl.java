package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LendingJpaRepoImpl implements LendingRepository {

    private final EntityManager em;
    
    @Override
    public Optional<Lending> findByLendingNumber(String lendingNumber) {
        return Optional.empty();
    }

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn) {
        return null;
    }

    @Override
    public int getCountFromCurrentYear() {
        return 0;
    }

    @Override
    public List<Lending> listOutstandingByReaderNumber(String readerNumber) {
        return null;
    }

    @Override
    public Double getAverageDuration() {
        return null;
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        return null;
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        return null;
    }

    @Override
    public List<Lending> searchLendings(Page page, String readerNumber, String isbn, Boolean returned, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Lending save(Lending entity) {
        return null;
    }

    @Override
    public void delete(Lending entity) {

    }

    @Override
    public List<Lending> findAll() {
        return null;
    }

    @Override
    public Lending findById(Long aLong) {
        return null;
    }
}
