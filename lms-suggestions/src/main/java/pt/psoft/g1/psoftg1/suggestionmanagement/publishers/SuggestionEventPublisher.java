package pt.psoft.g1.psoftg1.suggestionmanagement.publishers;

import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

public interface SuggestionEventPublisher {
    void sendSuggestionCreated(Suggestion suggestion);
    void sendSuggestionUpdated(Suggestion suggestion, Long currentVersion);
    void sendSuggestionDeleted(Suggestion suggestion, Long currentVersion);
}
