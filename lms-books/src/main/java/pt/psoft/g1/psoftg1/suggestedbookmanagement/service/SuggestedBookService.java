package pt.psoft.g1.psoftg1.suggestedbookmanagement.service;

import pt.psoft.g1.psoftg1.suggestedbookmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;

public interface SuggestedBookService {

    SuggestedBook createSuggestedBook(SuggestionViewAMQP suggestionViewAMQP);
}
