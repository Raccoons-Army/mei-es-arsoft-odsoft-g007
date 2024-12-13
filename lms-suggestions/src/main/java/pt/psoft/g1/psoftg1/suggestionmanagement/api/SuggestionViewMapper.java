package pt.psoft.g1.psoftg1.suggestionmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SuggestionViewMapper extends MapperInterface {

    @Mapping(target = "suggestionId", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "readerNumber", expression = "java(suggestion.getReaderDetails().getReaderNumber())")
    public abstract SuggestionView toSuggestionView(Suggestion suggestion);
    public abstract List<SuggestionView> toSuggestionView(List<Suggestion> suggestionList);
}
