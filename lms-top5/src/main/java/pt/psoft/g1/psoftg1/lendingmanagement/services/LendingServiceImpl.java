package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.util.DateUtils;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LendingServiceImpl implements LendingService {
    private final LendingRepository lendingRepository;
    private final BookRepository bookRepository;

    private final IdGenerationStrategy<String> idGenerationStrategy;

    @Override
    public Lending create(LendingViewAMQP resource) {
        if (lendingRepository.findByLendingNumber(resource.getLendingNumber()).isPresent()) {
            throw new ConflictException("Lending with number " + resource.getLendingNumber() + " already exists");
        }

        final var b = bookRepository.findByIsbn(resource.getBookIsbn())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        final Lending l = new Lending(idGenerationStrategy.generateId(), b, resource.getLendingNumber(),
                LocalDate.parse(resource.getStartDate(), DateUtils.ISO_DATE_FORMATTER),
                LocalDate.parse(resource.getLimitDate(), DateUtils.ISO_DATE_FORMATTER));

        return lendingRepository.save(l);
    }

    @Override
    public Lending update(LendingViewAMQP resource) {
        final var l = lendingRepository.findByLendingNumber(resource.getLendingNumber())
                .orElseThrow(() -> new NotFoundException("Lending not found"));
        l.setReturned(resource.getVersion());

        return lendingRepository.save(l);
    }
}
