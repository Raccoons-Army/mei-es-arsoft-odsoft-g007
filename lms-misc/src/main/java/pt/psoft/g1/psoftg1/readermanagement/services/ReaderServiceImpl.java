package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepo;

    @Override
    public ReaderDetails create(ReaderViewAMQP readerViewAMQP) {

        String email = readerViewAMQP.getUsername();
        String readerNumber = readerViewAMQP.getReaderNumber();

        return create(email, readerNumber);
    }

    private ReaderDetails create(String email, String readerNumber) {
        if (readerRepo.findByUsername(email).isPresent()) {
            throw new ConflictException("Username already exists!");
        }

        ReaderDetails rd = new ReaderDetails(readerNumber, email);

        return readerRepo.save(rd);
    }

    @Override
    public void delete(ReaderViewAMQP readerViewAMQP) {
        final ReaderDetails readerDetails = readerRepo.findByReaderNumber(readerViewAMQP.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        readerRepo.delete(readerDetails);
    }


    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return this.readerRepo.findByReaderNumber(readerNumber);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }

}
