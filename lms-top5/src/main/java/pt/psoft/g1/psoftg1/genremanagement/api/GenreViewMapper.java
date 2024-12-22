package pt.psoft.g1.psoftg1.genremanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.model.TopGenre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreCountDTO;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GenreViewMapper extends MapperInterface{


    @Mapping(target = "genre", expression = "java(genreBookCountView.getGenre())")
    @Mapping(target = "lendingCount", source = "lendingCount")
    public abstract GenreCountDTO toGenreCountDTO(TopGenre genreBookCountView);
    public abstract List<GenreCountDTO> toGenreCountDTO(List<TopGenre> genreBookCountView);
}
