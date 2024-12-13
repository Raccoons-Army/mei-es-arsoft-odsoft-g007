package pt.psoft.g1.psoftg1.suggestionmanagement.services;

import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

public interface SuggestionService {

    Suggestion createSuggestion(CreateSuggestionRequest createSuggestionRequest);

    Suggestion createSuggestion(SuggestionViewAMQP resource);

    Suggestion updateSuggestion(SuggestionViewAMQP resource);
}
