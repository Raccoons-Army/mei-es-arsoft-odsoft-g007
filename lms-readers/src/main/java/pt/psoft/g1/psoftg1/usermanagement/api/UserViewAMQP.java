package pt.psoft.g1.psoftg1.usermanagement.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "A User form AMQP communication")
public class UserViewAMQP {
    private Long version;
    private String username;
    private Set<String> authorities = new HashSet<>();
}
