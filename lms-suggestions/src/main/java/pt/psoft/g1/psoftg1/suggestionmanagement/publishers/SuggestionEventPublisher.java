package pt.psoft.g1.psoftg1.suggestionmanagement.publishers;

import pt.psoft.g1.psoftg1.suggestionmanagement.api.SuggestionViewAMQP;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

public interface SuggestionEventPublisher {
    SuggestionViewAMQP sendSuggestionCreated(Suggestion suggestion);
    SuggestionViewAMQP sendSuggestionUpdated(Suggestion suggestion, Long currentVersion);
    SuggestionViewAMQP sendSuggestionDeleted(Suggestion suggestion, Long currentVersion);
}
