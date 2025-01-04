package pt.psoft.g1.psoftg1.usermanagement.api;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserViewAMQPMapper extends MapperInterface {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "authorities", source = "authorities")
    public abstract UserViewAMQP toUserViewAMQP(User user);

    public abstract List<UserViewAMQP> toUserViewAMQP(List<User> userList);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "authorities", source = "authorities")
    public abstract User toUser(UserViewAMQP userViewAMQP);

    public abstract List<User> toUser(List<UserViewAMQP> userViewAMQPList);

    // Custom mapping method for authorities
    protected Set<Role> mapAuthorities(Set<String> authorities) {
        return authorities.stream()
                .map(Role::new)
                .collect(Collectors.toSet());
    }

    protected Set<String> mapRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());
    }
}
