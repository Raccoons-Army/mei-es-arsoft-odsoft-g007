package pt.psoft.g1.psoftg1.genremanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

@Mapper(componentModel = "spring")
public abstract class GenreMapper {

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract MongoGenreModel toMongoGenre(Genre genre);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract Genre fromMongoGenre(MongoGenreModel mongoGenre);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract JpaGenreModel toJpaGenre(Genre genre);

    @Mapping(source = "pk", target = "pk")
    @Mapping(source = "genre", target = "genre")
    public abstract Genre fromJpaGenre(JpaGenreModel jpaGenre);
}
