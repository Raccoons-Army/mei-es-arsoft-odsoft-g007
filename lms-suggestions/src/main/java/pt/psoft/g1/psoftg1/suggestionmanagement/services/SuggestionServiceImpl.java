package pt.psoft.g1.psoftg1.suggestionmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.publishers.SuggestionEventPublisher;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final ReaderRepository readerRepository;
    private final IdGenerationStrategy<String> idGenerationStrategy;
    private final SuggestionEventPublisher suggestionEventPublisher;

    @Override
    public Suggestion createSuggestion(CreateSuggestionRequest resource) {

        Optional<Suggestion> existsSuggestion = suggestionRepository.findByIsbn(resource.getIsbn());
        if (existsSuggestion.isPresent()) {
            throw new ConflictException("Suggestion for Book with ISBN " + resource.getIsbn() + " already exists");
        }

        final var reader = readerRepository.findByReaderNumber(resource.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        final Suggestion suggestion = new Suggestion(idGenerationStrategy.generateId(), resource.getIsbn(), LocalDate.now(), reader);

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        if (savedSuggestion != null) {
            suggestionEventPublisher.sendSuggestionCreated(savedSuggestion);
        }

        return savedSuggestion;
    }

    @Override
    public Suggestion createSuggestion(SuggestionViewAMQP resource) {
        Optional<Suggestion> existsSuggestion = suggestionRepository.findByIsbn(resource.getBookIsbn());
        if (existsSuggestion.isPresent()) {
            throw new ConflictException("Suggestion for Book with ISBN " + resource.getBookIsbn() + " already exists");
        }

        final var reader = readerRepository.findByReaderNumber(resource.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        final Suggestion suggestion = new Suggestion(idGenerationStrategy.generateId(), resource.getBookIsbn(), LocalDate.now(), reader);

        return suggestionRepository.save(suggestion);
    }

    @Override
    public Suggestion updateSuggestion(SuggestionViewAMQP resource) {
        final var suggestion = suggestionRepository.findById(resource.getSuggestionId())
                .orElseThrow(() -> new NotFoundException("Suggestion not found"));

        Suggestion updatedSuggestion = suggestionRepository.save(suggestion);

        if (updatedSuggestion != null) {
            suggestionEventPublisher.sendSuggestionUpdated(updatedSuggestion, updatedSuggestion.getVersion());
        }

        return updatedSuggestion;
    }
}
