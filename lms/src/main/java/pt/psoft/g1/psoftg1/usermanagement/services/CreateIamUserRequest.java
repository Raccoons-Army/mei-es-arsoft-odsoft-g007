package pt.psoft.g1.psoftg1.usermanagement.services;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class CreateIamUserRequest {

    @NonNull
    @NotBlank
    @Email
    @Setter
    @Getter
    private String username;

    @NonNull
    @NotBlank
    private String name;

    @Getter
    @Setter
    private String role;

    private Set<String> authorities = new HashSet<>();

    public CreateIamUserRequest(final String username, final String fullName) {
        this.username = username;
        this.name = fullName;
    }
}
