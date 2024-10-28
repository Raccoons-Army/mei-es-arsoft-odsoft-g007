package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaUserModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoUserModel;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract JpaUserModel toJpaUserModel(User user);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract User fromJpaUserModel(JpaUserModel jpaUserModel);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract MongoUserModel toMongoUserModel(User user);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract User fromMongoUserModel(MongoUserModel mongoUserModel);
    
    public String map(Name name) {
        return name != null ? name.toString() : null;
    }

    public Name map(String name) {
        return new Name(name);
    }
}
