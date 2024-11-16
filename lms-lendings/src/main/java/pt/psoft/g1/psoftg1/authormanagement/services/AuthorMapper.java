package pt.psoft.g1.psoftg1.authormanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

@Mapper(componentModel = "spring", implementationName = "CustomAuthorMapperImpl")
public abstract class AuthorMapper extends MapperInterface {
    @Mapping(target = "photo", source = "request.photoURI")
    @Mapping(target = "authorNumber", source = "authorNumber")
    public abstract Author create(String authorNumber, CreateAuthorRequest request);

    public abstract void update(UpdateAuthorRequest request, @MappingTarget Author author);

}
