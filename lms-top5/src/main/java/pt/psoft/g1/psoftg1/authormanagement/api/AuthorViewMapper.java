package pt.psoft.g1.psoftg1.authormanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.TopAuthor;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorCountDTO;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AuthorViewMapper extends MapperInterface {
    @Mapping(target = "name", source = "name")
    @Mapping(target = "lendingCount", source = "lendingCount")
    public abstract AuthorCountDTO toAuthorView(TopAuthor author);
    public abstract List<AuthorCountDTO> toAuthorView(List<TopAuthor> authors);
}