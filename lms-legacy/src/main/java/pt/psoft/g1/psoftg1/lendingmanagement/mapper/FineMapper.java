package pt.psoft.g1.psoftg1.lendingmanagement.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.JpaFineModel;
import pt.psoft.g1.psoftg1.lendingmanagement.dbSchema.MongoFineModel;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LendingMapper.class})
public abstract class FineMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    @Mapping(target = "centsValue", source = "centsValue")
    @Mapping(target = "lending", source = "lending")
    public abstract Fine fromJpaFineModel(JpaFineModel jpaFineModel);
    public abstract List<Fine> fromJpaFineModel(List<JpaFineModel> jpaFineModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    @Mapping(target = "centsValue", source = "centsValue")
    @Mapping(target = "lending", source = "lending")
    public abstract JpaFineModel toJpaFineModel(Fine fine);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    @Mapping(target = "centsValue", source = "centsValue")
    @Mapping(target = "lending", source = "lending")
    public abstract Fine fromMongoFineModel(MongoFineModel mongoFineModel);
    public abstract List<Fine> fromMongoFineModel(List<MongoFineModel> mongoFineModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "fineValuePerDayInCents", source = "fineValuePerDayInCents")
    @Mapping(target = "centsValue", source = "centsValue")
    @Mapping(target = "lending", source = "lending")
    public abstract MongoFineModel toMongoFineModel(Fine fine);
}
