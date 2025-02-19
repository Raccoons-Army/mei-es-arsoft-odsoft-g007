package pt.psoft.g1.psoftg1.suggestionmanagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A DTO for creating a Suggestion")
public class CreateSuggestionRequest {

    @NotNull
    @NotBlank
    @Size(min = 10, max = 13)
    private String isbn;

    @NotNull
    @NotBlank
    private String readerNumber;
}
