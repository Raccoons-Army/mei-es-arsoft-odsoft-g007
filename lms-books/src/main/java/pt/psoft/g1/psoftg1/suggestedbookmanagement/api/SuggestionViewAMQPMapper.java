package pt.psoft.g1.psoftg1.suggestedbookmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.suggestedbookmanagement.model.SuggestedBook;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SuggestionViewAMQPMapper extends MapperInterface {

    @Mapping(target = "suggestionId", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract SuggestionViewAMQP toSuggestionViewAMQP(SuggestedBook suggestion);
    public abstract List<SuggestionViewAMQP> toSuggestionViewAMQP(List<SuggestedBook> suggestionList);


    @Mapping(target = "pk", source = "suggestionId")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "version", source = "version")
    public abstract SuggestedBook toSuggestedBook(SuggestionViewAMQP suggestionViewAMQP);
    public abstract List<SuggestedBook> toSuggestedBook(List<SuggestionViewAMQP> suggestionViewAMQPs);


}
