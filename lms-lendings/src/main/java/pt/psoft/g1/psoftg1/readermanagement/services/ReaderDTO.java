package pt.psoft.g1.psoftg1.readermanagement.services;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderDTO {

    @NotNull
    private String username;

    @NotNull
    private String readerNumber;

    public ReaderDTO() {
    }

    public ReaderDTO(String username, String readerNumber) {
        this.username = username;
        this.readerNumber = readerNumber;
    }
}
