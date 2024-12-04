package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract MongoBookModel toMongoBookModel(Book book);

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract JpaBookModel toJpaBookModel(Book book);

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract Book fromMongoBookModel(MongoBookModel mongoBookModel);
    public abstract List<Book> fromMongoBookModel(List<MongoBookModel> mongoBookModel);

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract Book fromJpaBookModel(JpaBookModel jpaBookModel);
    public abstract List<Book> fromJpaBookModel(List<JpaBookModel> jpaBookModel);

}
