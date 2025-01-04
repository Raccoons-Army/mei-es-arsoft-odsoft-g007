package pt.psoft.g1.psoftg1.readermanagement.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "A Reader Details form AMQP communication")
public class ReaderDetailsViewAMQP {

    @NotNull
    private String username;

    @NotNull
    private String readerNumber;

    @NotNull
    private String birthDate;

    @NotNull
    private String phoneNumber;

    @NotNull
    private boolean gdprConsent;

    @NotNull
    private boolean marketingConsent;

    @NotNull
    private boolean thirdPartySharingConsent;

    @NotNull
    private List<String> interestList;

    private String photo;

    @NotNull
    private Long version;

    @Setter
    @Getter
    private Map<String, Object> _links = new HashMap<>();
}
