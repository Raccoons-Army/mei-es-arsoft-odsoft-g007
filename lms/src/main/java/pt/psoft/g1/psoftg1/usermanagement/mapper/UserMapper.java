package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "authorities", target = "authorities")
    public abstract JpaUserModel toJpaUserModel(User user);

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

}
