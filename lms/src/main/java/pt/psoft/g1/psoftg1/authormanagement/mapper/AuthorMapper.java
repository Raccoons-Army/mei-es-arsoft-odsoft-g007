package pt.psoft.g1.psoftg1.authormanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaPhotoModel;
import pt.psoft.g1.psoftg1.shared.dbSchema.MongoPhotoModel;
import pt.psoft.g1.psoftg1.shared.mapper.PhotoMapper;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.nio.file.Path;

@Mapper(componentModel = "spring", uses = {PhotoMapper.class})
public abstract class AuthorMapper {

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "photoURI", source = "photo")
    public abstract MongoAuthorModel toMongoAuthor(Author author);

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "photo", source = "photo")
    public abstract Author fromMongoAuthor(MongoAuthorModel mongoAuthor);

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "photoURI", source = "photo")
    public abstract JpaAuthorModel toJpaAuthor(Author author);

    @Mapping(source = "authorNumber", target = "authorNumber")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "version", target = "version")
    @Mapping(target = "photo", source = "photo")
    public abstract Author fromJpaAuthor(JpaAuthorModel jpaAuthor);
}
