package pt.psoft.g1.psoftg1.suggestionmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A Suggestion")
public class SuggestionView {

    private String suggestionId;

    @NotNull
    private String isbn;

    @NotNull
    private String readerNumber;

    @NotNull
    private String createdAt;
}
