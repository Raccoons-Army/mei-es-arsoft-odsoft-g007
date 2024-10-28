package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class LibrarianMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract JpaReaderModel toJpaReaderModel(Librarian librarian);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract Librarian fromJpaReaderModel(JpaReaderModel jpaReaderModel);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract MongoReaderModel toMongoReaderModel(Librarian librarian);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    public abstract Librarian fromMongoReaderModel(MongoReaderModel mongoReaderModel);


}
