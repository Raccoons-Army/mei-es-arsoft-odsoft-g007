package pt.psoft.g1.psoftg1.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaForbiddenNameModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoForbiddenNameModel;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;

@Mapper(componentModel = "spring")
public abstract class ForbiddenNameMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "forbiddenName", source = "forbiddenName")
    public abstract ForbiddenName toForbiddenName(JpaForbiddenNameModel jpaForbiddenNameModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "forbiddenName", source = "forbiddenName")
    public abstract JpaForbiddenNameModel toJpaForbiddenNameModel(ForbiddenName forbiddenName);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "forbiddenName", source = "forbiddenName")
    public abstract ForbiddenName toForbiddenName(MongoForbiddenNameModel mongoForbiddenNameModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "forbiddenName", source = "forbiddenName")
    public abstract MongoForbiddenNameModel toMongoForbiddenNameModel(ForbiddenName forbiddenName);
}
