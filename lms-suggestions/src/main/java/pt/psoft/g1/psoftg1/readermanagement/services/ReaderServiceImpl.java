package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepo;

    @Override
    public void create(ReaderDetailsViewAMQP request) {
        if (readerRepo.findByReaderNumber(request.getReaderNumber()).isPresent()) {
            throw new ConflictException("ReaderDetails with number " + request.getReaderNumber() + " already exists");
        }
        ReaderDetails rd = new ReaderDetails(request.getReaderNumber());
        readerRepo.save(rd);
    }
}