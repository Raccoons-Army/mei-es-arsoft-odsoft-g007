package pt.psoft.g1.psoftg1.suggestedbookmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.repositories.SuggestedBookRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestedBookServiceImpl implements SuggestedBookService {

    private final SuggestedBookRepository suggestedBookRepository;

    @Override
    public SuggestedBook createSuggestedBook(SuggestionViewAMQP resource) {
        Optional<SuggestedBook> existsSuggestedBook = suggestedBookRepository.findByIsbn(resource.getBookIsbn());
        if (existsSuggestedBook.isPresent()) {
            throw new ConflictException("Suggestion for Book with ISBN " + resource.getBookIsbn() + " already exists");
        }

        final SuggestedBook suggestedBook = new SuggestedBook(resource.getBookIsbn());

        return suggestedBookRepository.save(suggestedBook);
    }
}
