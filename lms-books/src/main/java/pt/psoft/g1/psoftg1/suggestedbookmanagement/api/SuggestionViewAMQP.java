package pt.psoft.g1.psoftg1.suggestedbookmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A Suggestion form AMQP communication")
public class SuggestionViewAMQP {

    @NotNull
    private String suggestionId;

    @NotNull
    private String bookIsbn;

    @NotNull
    private String readerNumber;

    @NotNull
    private String suggestionDate;

    @NotNull
    private Long version;
}
