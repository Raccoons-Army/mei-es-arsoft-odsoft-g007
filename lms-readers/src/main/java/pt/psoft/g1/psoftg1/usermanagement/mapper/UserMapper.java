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

    @Mapping(source = "username", target = "username")
    public abstract JpaUserModel toJpaUserModel(User user);
    public abstract List<JpaUserModel> toJpaUserModel(List<User> user);

    @Mapping(source = "username", target = "username")
    public abstract User fromJpaUserModel(JpaUserModel jpaUserModel);
    public abstract List<User> fromJpaUserModel(List<JpaUserModel> jpaUserModel);

    @Mapping(source = "username", target = "username")
    public abstract MongoUserModel toMongoUserModel(User user);

    @Mapping(source = "username", target = "username")
    public abstract User fromMongoUserModel(MongoUserModel mongoUserModel);
    public abstract List<User> fromMongoUserModel(List<MongoUserModel> mongoUserModel);

}
