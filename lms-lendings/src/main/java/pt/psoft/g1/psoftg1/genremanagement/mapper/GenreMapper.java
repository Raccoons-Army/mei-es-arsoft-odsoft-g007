package pt.psoft.g1.psoftg1.genremanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GenreMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract MongoGenreModel toMongoGenre(Genre genre);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract Genre fromMongoGenre(MongoGenreModel mongoGenre);
    public abstract List<Genre> fromMongoGenre(List<MongoGenreModel> mongoGenre);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract JpaGenreModel toJpaGenre(Genre genre);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract Genre fromJpaGenre(JpaGenreModel jpaGenre);
    public abstract List<Genre> fromJpaGenre(List<JpaGenreModel> jpaGenre);
}
