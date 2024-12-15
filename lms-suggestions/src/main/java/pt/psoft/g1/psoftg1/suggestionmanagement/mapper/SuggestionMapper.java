package pt.psoft.g1.psoftg1.suggestionmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;
import pt.psoft.g1.psoftg1.suggestionmanagement.dbSchema.JpaSuggestionModel;
import pt.psoft.g1.psoftg1.suggestionmanagement.dbSchema.MongoSuggestionModel;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ReaderDetailsMapper.class})
public abstract class SuggestionMapper {


    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract JpaSuggestionModel toJpaSuggestionModel(Suggestion suggestion);


    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract Suggestion fromJpaSuggestionModel(JpaSuggestionModel jpaSuggestionModel);
    public abstract List<Suggestion> fromJpaSuggestionModel(List<JpaSuggestionModel> jpaSuggestions);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract MongoSuggestionModel toMongoSuggestionModel(Suggestion suggestion);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    public abstract Suggestion fromMongoSuggestionModel(MongoSuggestionModel mongoSuggestionModel);
    public abstract List<Suggestion> fromMongoSuggestionModel(List<MongoSuggestionModel> mongoSuggestions);

    // Custom mapping methods for Isbn <-> String
    protected String map(Isbn value) {
        return value != null ? value.toString() : null;
    }

    protected Isbn map(String value) {
        return value != null ? new Isbn(value) : null;
    }
}
