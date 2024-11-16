package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ReaderMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract JpaReaderModel toJpaReaderModel(Reader reader);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract Reader fromJpaReaderModel(JpaReaderModel jpaReaderModel);
    public abstract List<Reader> fromJpaReaderModel(List<JpaReaderModel> jpaReaderModel);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract MongoReaderModel toMongoReaderModel(Reader reader);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract Reader fromMongoReaderModel(MongoReaderModel mongoReaderModel);
    public abstract List<Reader> fromMongoReaderModel(List<MongoReaderModel> mongoReaderModel);


}
