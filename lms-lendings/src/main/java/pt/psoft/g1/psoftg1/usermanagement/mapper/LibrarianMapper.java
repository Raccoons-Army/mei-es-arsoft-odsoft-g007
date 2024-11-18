package pt.psoft.g1.psoftg1.usermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.JpaReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.dbSchema.MongoReaderModel;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class LibrarianMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    public abstract JpaReaderModel toJpaReaderModel(Librarian librarian);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    public abstract Librarian fromJpaReaderModel(JpaReaderModel jpaReaderModel);
    public abstract List<Librarian> fromJpaReaderModel(List<JpaReaderModel> jpaReaderModel);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    public abstract MongoReaderModel toMongoReaderModel(Librarian librarian);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "username", target = "username")
    public abstract Librarian fromMongoReaderModel(MongoReaderModel mongoReaderModel);
    public abstract List<Librarian> fromMongoReaderModel(List<MongoReaderModel> mongoReaderModel);


}
