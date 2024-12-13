package pt.psoft.g1.psoftg1.suggestionmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.shared.utils.DateUtils;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SuggestionViewAMQPMapper extends MapperInterface {

    @Mapping(target = "suggestionId", source = "pk")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "readerNumber", expression = "java(suggestion.getReaderDetails().getReaderNumber())")
    @Mapping(target = "createdAt", expression = "java(formatLocalDate(suggestion.getCreatedAt()))")
    @Mapping(target = "version", source = "version")
    public abstract SuggestionViewAMQP toSuggestionViewAMQP(Suggestion suggestion);
    public abstract List<SuggestionViewAMQP> toSuggestionViewAMQP(List<Suggestion> suggestionList);

    // Utility method to format LocalDate -> String
    protected String formatLocalDate(LocalDate date) {
        return date != null ? DateUtils.ISO_DATE_FORMATTER.format(date) : null;
    }
}
