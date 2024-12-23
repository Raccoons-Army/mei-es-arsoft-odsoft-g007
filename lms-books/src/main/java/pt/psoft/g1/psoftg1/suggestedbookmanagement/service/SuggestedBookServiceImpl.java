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
        final SuggestedBook suggestedBook = new SuggestedBook(resource.getIsbn());
        return suggestedBookRepository.save(suggestedBook);
    }
}
