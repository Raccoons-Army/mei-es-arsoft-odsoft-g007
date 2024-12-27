package pt.psoft.g1.psoftg1.usermanagement.services;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private String username;
    private String name;
    private Set<String> roles;
    private String password;

    // Constructors
    public UserDTO() {}

    public UserDTO(String username, String name, Set<String> roles, String password) {
        this.username = username;
        this.name = name;
        this.roles = roles;
        this.password = password;
    }
}

