package pt.psoft.g1.psoftg1.recommendationmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "A Recommendation form AMQP communication")
public class RecommendationViewAMQP {
    @NotNull
    private String isbn;

    @NotNull
    private String readerNumber;

    @NotNull
    private boolean positive;

    @NotNull
    private Long version;
}
