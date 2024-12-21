package pt.psoft.g1.psoftg1.lendingmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.shared.util.DateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class LendingServiceImpl implements LendingService {
    private final LendingRepository lendingRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    private final IdGenerationStrategy<String> idGenerationStrategy;

    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    @Override
    public List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn, Optional<Boolean> returned) {
        List<Lending> lendings = lendingRepository.listByReaderNumberAndIsbn(readerNumber, isbn);
        if (returned.isEmpty()) {
            return lendings;
        } else {
            for (int i = 0; i < lendings.size(); i++) {
                if ((lendings.get(i).getReturnedDate() == null) == returned.get()) {
                    lendings.remove(i);
                    i--;
                }
            }
        }
        return lendings;
    }

    @Override
    public Lending create(LendingViewAMQP resource) {
        if (lendingRepository.findByLendingNumber(resource.getLendingNumber()).isPresent()) {
            throw new ConflictException("Lending with number " + resource.getLendingNumber() + " already exists");
        }

        final var b = bookRepository.findByIsbn(resource.getBookIsbn())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        final var r = readerRepository.findByReaderNumber(resource.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Reader not found"));
        final Lending l = new Lending(idGenerationStrategy.generateId(), b, r, resource.getLendingNumber(),
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

    @Override
    public Double getAverageDuration() {
        Double avg = lendingRepository.getAverageDuration();
        return Double.valueOf(String.format(Locale.US, "%.1f", avg));
    }

    @Override
    public List<Lending> getOverdue(Page page) {
        if (page == null) {
            page = new Page(1, 10);
        }
        return lendingRepository.getOverdue(page);
    }

    @Override
    public Double getAvgLendingDurationByIsbn(String isbn) {
        Double avg = lendingRepository.getAvgLendingDurationByIsbn(isbn);
        return Double.valueOf(String.format(Locale.US, "%.1f", avg));
    }
}
