package pt.psoft.g1.psoftg1.authormanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper {

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract MongoAuthorModel toMongoAuthor(Author author);

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract Author fromMongoAuthor(MongoAuthorModel mongoAuthor);
    public abstract List<Author> fromMongoAuthor(List<MongoAuthorModel> mongoAuthor);


    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract JpaAuthorModel toJpaAuthor(Author author);

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "version", target = "version")
    public abstract Author fromJpaAuthor(JpaAuthorModel jpaAuthor);
    public abstract List<Author> fromJpaAuthor(List<JpaAuthorModel> jpaAuthor);
}
