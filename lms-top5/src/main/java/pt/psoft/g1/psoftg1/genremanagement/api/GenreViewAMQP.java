package pt.psoft.g1.psoftg1.genremanagement.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "A Book form AMQP communication")
public class GenreViewAMQP {
    @NotNull
    private String genre;

    @Setter
    @Getter
    private Map<String, Object> _links = new HashMap<>();
}
