package pt.psoft.g1.psoftg1.lendingmanagement.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.readermanagement.mapper.ReaderDetailsMapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class, ReaderDetailsMapper.class})
public abstract class LendingMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "commentary", source = "commentary")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    public abstract JpaLendingModel toJpaLendingModel(Lending lending);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "commentary", source = "commentary")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    public abstract Lending fromJpaLendingModel(JpaLendingModel jpaLendingModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "commentary", source = "commentary")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    public abstract MongoLendingModel toMongoLendingModel(Lending lending);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "readerDetails", source = "readerDetails")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "commentary", source = "commentary")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    public abstract Lending fromMongoLendingModel(MongoLendingModel mongoLendingModel);

    /**
     * Custom mapping method to convert String to LendingNumber.
     * MapStruct will use this method for mapping lendingNumber fields.
     */
    public LendingNumber map(String lendingNumber) {
        return new LendingNumber(lendingNumber);
    }

    /**
     * Custom mapping method to convert LendingNumber to String.
     */
    public String map(LendingNumber lendingNumber) {
        return lendingNumber != null ? lendingNumber.toString() : null;
    }
}
