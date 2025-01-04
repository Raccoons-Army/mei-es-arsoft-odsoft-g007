package pt.psoft.g1.psoftg1.readermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderNumber;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderDTO;
import pt.psoft.g1.psoftg1.usermanagement.mapper.ReaderMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.FactoryUser;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ReaderMapper.class})
public abstract class ReaderDetailsMapper {

    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "version", source = "version")
    public abstract JpaReaderDetailsModel toJpaReaderDetailsModel(ReaderDetails readerDetails);

    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetails fromJpaReaderDetailsModel(JpaReaderDetailsModel jpaReaderDetailsModel);
    public abstract List<ReaderDetails> fromJpaReaderDetailsModel(List<JpaReaderDetailsModel> jpaReaderDetailsModel);

    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "version", source = "version")
    public abstract MongoReaderDetailsModel toMongoReaderDetailsModel(ReaderDetails readerDetails);

    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetails fromMongoReaderDetailsModel(MongoReaderDetailsModel mongoReaderDetailsModel);
    public abstract List<ReaderDetails> fromMongoReaderDetailsModel(List<MongoReaderDetailsModel> mongoReaderDetailsModel);

    // Custom mapping method
    ReaderNumber map(String value) {
        return value == null ? null : new ReaderNumber(value);
    }
}
