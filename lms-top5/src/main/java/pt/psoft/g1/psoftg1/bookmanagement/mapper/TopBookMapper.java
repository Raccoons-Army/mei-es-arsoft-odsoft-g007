package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaTopBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoTopBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TopBookMapper {

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors") 
    @Mapping(target = "description", source = "description")
    @Mapping(target = "lendingCount", source = "lendingCount")
    @Mapping(target = "version", source = "version")
    public abstract MongoTopBookModel toMongoTopBookModel(TopBook book);

    @Mapping(target = "version", source = "version")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "lendingCount", source = "lendingCount")
    @Mapping(target = "description", source = "description")
    public abstract JpaTopBookModel toJpaTopBookModel(TopBook book);

    @Mapping(target = "version", source = "version")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "lendingCount", source = "lendingCount")
    @Mapping(target = "description", source = "description")
    public abstract TopBook fromMongoTopBookModel(MongoTopBookModel mongoTopBookModel);
    public abstract List<TopBook> fromMongoTopBookModel(List<MongoTopBookModel> mongoTopBookModel);

    @Mapping(target = "version", source = "version")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "lendingCount", source = "lendingCount")
    @Mapping(target = "description", source = "description")
    public abstract TopBook fromJpaTopBookModel(JpaTopBookModel jpaTopBookModel);
    public abstract List<TopBook> fromJpaTopBookModel(List<JpaTopBookModel> jpaTopBookModel);

}
