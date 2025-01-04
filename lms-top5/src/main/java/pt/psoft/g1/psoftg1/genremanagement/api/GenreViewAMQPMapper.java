package pt.psoft.g1.psoftg1.genremanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GenreViewAMQPMapper extends MapperInterface {

    @Mapping(target = "genre", source = "genre")
    public abstract GenreViewAMQP toGenreViewAMQP(Genre genre);

    public abstract List<GenreViewAMQP> toGenreViewAMQP(List<Genre> genreList);

    @Mapping(target = "genre", source = "genre")
    public abstract Genre toGenre(GenreViewAMQP genreViewAMQP);

    public abstract List<Genre> toGenre(List<GenreViewAMQP> genreViewAMQPList);
}
