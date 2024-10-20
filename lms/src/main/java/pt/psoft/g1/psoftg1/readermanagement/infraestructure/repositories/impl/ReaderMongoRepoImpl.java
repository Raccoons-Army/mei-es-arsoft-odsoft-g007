package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ReaderMongoRepoImpl implements ReaderRepository {

    public final MongoTemplate mt;

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return Optional.empty();
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return null;
    }

    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<ReaderDetails> findByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public int getCountFromCurrentYear() {
        return 0;
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        return null;
    }


    @Override
    public ReaderDetails save(ReaderDetails entity) {
        return null;
    }

    @Override
    public void delete(ReaderDetails entity) {

    }

    @Override
    public List<ReaderDetails> findAll() {
        return null;
    }

    @Override
    public Optional<ReaderDetails> findById(Long aLong) {
        return Optional.empty();
    }
}
