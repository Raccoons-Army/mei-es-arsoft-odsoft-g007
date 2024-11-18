package pt.psoft.g1.psoftg1.readermanagement.services;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Data
@NoArgsConstructor
public class CreateReaderRequest {

    @NotBlank
    @NonNull
    private String readerNumber;
}
