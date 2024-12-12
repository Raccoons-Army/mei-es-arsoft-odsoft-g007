package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.mapper.AuthorMapper;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.shared.dbSchema.JpaPhotoModel;
import pt.psoft.g1.psoftg1.shared.mapper.PhotoMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, AuthorMapper.class, PhotoMapper.class})
public abstract class BookMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors") 
    @Mapping(target = "description", source = "description")
    @Mapping(target = "photoURI", source = "photo")
    @Mapping(target = "version", source = "version")
    public abstract MongoBookModel toMongoBookModel(Book book);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors") 
    @Mapping(target = "description", source = "description")
    @Mapping(target = "photoURI", source = "photo") 
    public abstract JpaBookModel toJpaBookModel(Book book);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors") 
    @Mapping(target = "description", source = "description")
    @Mapping(target = "photo", source = "photo")
    public abstract Book fromMongoBookModel(MongoBookModel mongoBookModel);
    public abstract List<Book> fromMongoBookModel(List<MongoBookModel> mongoBookModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors") 
    @Mapping(target = "description", source = "description")
    @Mapping(target = "photo", source = "photo")
    public abstract Book fromJpaBookModel(JpaBookModel jpaBookModel);
    public abstract List<Book> fromJpaBookModel(List<JpaBookModel> jpaBookModel);

}
