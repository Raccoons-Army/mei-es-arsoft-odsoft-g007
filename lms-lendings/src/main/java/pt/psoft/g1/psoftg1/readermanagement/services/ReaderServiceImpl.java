package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepo;
    private final ReaderMapper readerMapper;

    @Override
    public void create(CreateReaderRequest request) {
        if (readerRepo.findByReaderNumber(request.getReaderNumber()).isPresent()) {
            return;
        }
        ReaderDetails rd = readerMapper.createReaderDetails(request);

        readerRepo.save(rd);
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return this.readerRepo.findAll();
    }

    @Override
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }
}
