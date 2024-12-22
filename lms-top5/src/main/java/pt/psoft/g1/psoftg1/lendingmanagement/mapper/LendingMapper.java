package pt.psoft.g1.psoftg1.lendingmanagement.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.mapper.BookMapper;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoLendingModel;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public abstract class LendingMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    public abstract JpaLendingModel toJpaLendingModel(Lending lending);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    public abstract Lending fromJpaLendingModel(JpaLendingModel jpaLendingModel);
    public abstract List<Lending> fromJpaLendingModel(List<JpaLendingModel> jpaLendingModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    public abstract MongoLendingModel toMongoLendingModel(Lending lending);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "book", source = "book")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "returnedDate", source = "returnedDate")
    @Mapping(target = "version", source = "version")
    public abstract Lending fromMongoLendingModel(MongoLendingModel mongoLendingModel);
    public abstract List<Lending> fromMongoLendingModel(List<MongoLendingModel> mongoLendingModel);

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
