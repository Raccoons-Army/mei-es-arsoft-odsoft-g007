package pt.psoft.g1.psoftg1.suggestedbookmanagement.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "A Suggestion form AMQP communication")
public class SuggestionViewAMQP {

    @NotNull
    private String suggestionId;

    @NotNull
    private String isbn;

    @NotNull
    private String readerNumber;

    @NotNull
    private String createdAt;

    @NotNull
    private Long version;
}
