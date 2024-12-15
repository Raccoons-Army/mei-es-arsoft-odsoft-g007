package pt.psoft.g1.psoftg1.suggestionmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.CreateSuggestionRequest;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionService;

@Tag(name = "Suggestion", description = "Endpoints for managing Book Suggestions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suggestions")
public class SuggestionController {

    private final SuggestionService suggestionService;
    private final SuggestionViewMapper suggestionViewMapper;

    @Operation(summary = "Creates a new Suggestion")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SuggestionView> createSuggestion(@Valid @RequestBody final CreateSuggestionRequest resource) {

        final var suggestion = suggestionService.createSuggestion(resource);

        final var newSuggestionUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(suggestion.getPk())
                .build().toUri();

        return ResponseEntity.created(newSuggestionUri)
                .contentType(MediaType.parseMediaType("application/hal+json"))
                .eTag(Long.toString(suggestion.getVersion()))
                .body(suggestionViewMapper.toSuggestionView(suggestion));
    }
}
