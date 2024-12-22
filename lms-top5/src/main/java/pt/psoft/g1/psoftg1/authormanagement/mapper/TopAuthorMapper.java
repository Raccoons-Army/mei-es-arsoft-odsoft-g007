package pt.psoft.g1.psoftg1.authormanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaTopAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoTopAuthorModel;
import pt.psoft.g1.psoftg1.authormanagement.model.TopAuthor;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TopAuthorMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract MongoTopAuthorModel toMongoTopAuthor(TopAuthor topAuthor);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract TopAuthor fromMongoTopAuthor(MongoTopAuthorModel mongoTopAuthor);
    public abstract List<TopAuthor> fromMongoTopAuthor(List<MongoTopAuthorModel> mongoTopAuthor);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract JpaTopAuthorModel toJpaTopAuthor(TopAuthor topAuthor);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "lendingCount", target = "lendingCount")
    public abstract TopAuthor fromJpaTopAuthor(JpaTopAuthorModel jpaTopAuthor);
    public abstract List<TopAuthor> fromJpaTopAuthor(List<JpaTopAuthorModel> jpaTopAuthor);
}
