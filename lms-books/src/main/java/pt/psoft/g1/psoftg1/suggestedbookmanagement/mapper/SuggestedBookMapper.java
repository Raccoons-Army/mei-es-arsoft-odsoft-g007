package pt.psoft.g1.psoftg1.suggestedbookmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.dbSchema.JpaSuggestedBookModel;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.dbSchema.MongoSuggestedBookModel;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SuggestedBookMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract MongoSuggestedBookModel toMongoSuggestedBookModel(SuggestedBook suggestedBook);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract JpaSuggestedBookModel toJpaSuggestedBookModel(SuggestedBook suggestedBook);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract SuggestedBook fromMongoSuggestedBookModel(MongoSuggestedBookModel mongoSuggestedBookModel);
    public abstract List<SuggestedBook> fromMongoSuggestedBookModel(List<MongoSuggestedBookModel> mongoSuggestedBookModels);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract SuggestedBook fromJpaSuggestedBookModel(JpaSuggestedBookModel jpaSuggestedBookModel);
    public abstract List<SuggestedBook> fromJpaSuggestedBookModel(List<JpaSuggestedBookModel> jpaSuggestedBookModels);

    // Custom mapping methods for Isbn <-> String
    protected String map(Isbn value) {
        return value != null ? value.toString() : null;
    }
    protected Isbn map(String value) {
        return value != null ? new Isbn(value) : null;
    }
}
