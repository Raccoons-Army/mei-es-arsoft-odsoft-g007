package pt.psoft.g1.psoftg1.readermanagement.services;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Data
@NoArgsConstructor
public class CreateReaderRequest {

    @NotBlank
    @NonNull
    private String readerNumber;
}
