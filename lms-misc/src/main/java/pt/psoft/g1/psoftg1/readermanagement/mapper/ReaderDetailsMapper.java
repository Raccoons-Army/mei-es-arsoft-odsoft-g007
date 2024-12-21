package pt.psoft.g1.psoftg1.readermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", uses = {GenreMapper.class})
public abstract class ReaderDetailsMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "version", source = "version")
    public abstract JpaReaderDetailsModel toJpaReaderDetailsModel(ReaderDetails readerDetails);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetails fromJpaReaderDetailsModel(JpaReaderDetailsModel jpaReaderDetailsModel);
    public abstract List<ReaderDetails> fromJpaReaderDetailsModel(List<JpaReaderDetailsModel> jpaReaderDetailsModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "version", source = "version")
    public abstract MongoReaderDetailsModel toMongoReaderDetailsModel(ReaderDetails readerDetails);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetails fromMongoReaderDetailsModel(MongoReaderDetailsModel mongoReaderDetailsModel);
    public abstract List<ReaderDetails> fromMongoReaderDetailsModel(List<MongoReaderDetailsModel> mongoReaderDetailsModel);

}
