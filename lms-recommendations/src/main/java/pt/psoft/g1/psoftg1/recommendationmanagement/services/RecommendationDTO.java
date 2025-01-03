package pt.psoft.g1.psoftg1.recommendationmanagement.services;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class RecommendationDTO {

    private String isbn;

    private String readerNumber;

    private boolean positive;

    private String createdAt;
}
