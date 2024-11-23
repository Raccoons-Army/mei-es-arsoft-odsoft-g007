package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepo;
    private final FactoryUser _factoryUser;

    @Override
    public void create(ReaderDetailsViewAMQP request) {
        if (readerRepo.findByReaderNumber(request.getReaderNumber()).isPresent()) {
            throw new ConflictException("ReaderDetails with number " + request.getReaderNumber() + " already exists");
        }
        ReaderDetails rd = new ReaderDetails(request.getReaderNumber(), _factoryUser);
        rd.defineReader(request.getReaderUsername());

        readerRepo.save(rd);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }
}
