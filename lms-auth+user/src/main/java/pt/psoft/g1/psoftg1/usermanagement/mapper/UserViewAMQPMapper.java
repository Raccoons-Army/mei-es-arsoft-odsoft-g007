package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserViewAMQPMapper extends MapperInterface {

    @Mapping(target = "version", source = "version")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "authorities", source = "authorities")

    public abstract UserViewAMQP toUserViewAMQP(User user);
    public abstract List<UserViewAMQP> toUserViewAMQP(List<User> userList);
}
