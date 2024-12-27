package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "authorities", target = "authorities")
    public abstract JpaUserModel toJpaUserModel(User user);
    public abstract List<JpaUserModel> toJpaUserModel(List<User> user);

    public User fromJpaUserModel(JpaUserModel jpaUserModel){
        return User.newUser(
            jpaUserModel.getPk(),
            jpaUserModel.getUsername(),
            jpaUserModel.getPassword(),
            jpaUserModel.getName(),
            jpaUserModel.getAuthorities(),
            jpaUserModel.getVersion()
         );
    }
    public abstract List<User> fromJpaUserModel(List<JpaUserModel> jpaUserModel);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "authorities", target = "authorities")
    public abstract MongoUserModel toMongoUserModel(User user);


    public User fromMongoUserModel(MongoUserModel mongoUserModel){
        return User.newUser(
            mongoUserModel.getPk(),
            mongoUserModel.getUsername(),
            mongoUserModel.getPassword(),
            mongoUserModel.getName(),
            mongoUserModel.getAuthorities(),
            mongoUserModel.getVersion()
        );
    }
    public abstract List<User> fromMongoUserModel(List<MongoUserModel> mongoUserModel);
    
    public String map(Name name) {
        return name != null ? name.toString() : null;
    }

    public Name map(String name) {
        return new Name(name);
    }

    // Mapping User to UserDTO
    public UserDTO toDto(User user) {
        return new UserDTO(user.getUsername(), user.getName().getName(), mapRolesToStrings(user.getAuthorities()), user.getPassword());
    }

    // Mapping UserDTO to User
    public User toEntity(UserDTO userDTO){
        User user = new User(userDTO.getUsername(), userDTO.getName());
        user.setPasswordWithoutHashing(userDTO.getPassword());
        for(String role : userDTO.getRoles()) {
            user.addAuthority(new Role(role));
        }
        return user;
    }

    // Mapping List<User> to List<UserDTO>
    public List<UserDTO> toDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto) // Map each User to UserDTO
                .collect(Collectors.toList());
    }

    // Mapping List<UserDTO> to List<User>
    public List<User> toEntityList(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .map(this::toEntity) // Map each UserDTO to User
                .collect(Collectors.toList());
    }

    // Default method to handle Set<Role> -> Set<String>
    public Set<String> mapRolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());
    }
}
