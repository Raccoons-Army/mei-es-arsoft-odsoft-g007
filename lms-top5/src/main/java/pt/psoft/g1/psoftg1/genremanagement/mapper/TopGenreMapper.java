package pt.psoft.g1.psoftg1.genremanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaTopGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.MongoTopGenreModel;
import pt.psoft.g1.psoftg1.genremanagement.model.TopGenre;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TopGenreMapper {

    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract MongoTopGenreModel toMongoTopGenre(TopGenre genre);

    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract TopGenre fromMongoTopGenre(MongoTopGenreModel mongoTopGenre);
    public abstract List<TopGenre> fromMongoTopGenre(List<MongoTopGenreModel> mongoTopGenre);

    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract JpaTopGenreModel toJpaTopGenre(TopGenre genre);

    @Mapping(source = "genre", target = "genre")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract TopGenre fromJpaTopGenre(JpaTopGenreModel jpaTopGenre);
    public abstract List<TopGenre> fromJpaTopGenre(List<JpaTopGenreModel> jpaTopGenre);
}
