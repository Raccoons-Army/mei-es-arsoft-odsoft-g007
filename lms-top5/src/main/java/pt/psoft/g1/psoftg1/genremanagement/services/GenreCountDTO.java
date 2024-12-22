package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreCountDTO {
    private String genre;
    private Long lendingCount;
}
