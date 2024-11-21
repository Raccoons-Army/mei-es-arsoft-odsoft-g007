package pt.psoft.g1.psoftg1.lendingmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class LendingViewAMQPMapper extends MapperInterface {

    @Mapping(target = "lendingNumber", source = "lendingNumber")
    @Mapping(target = "bookIsbn", expression = "java(lending.getBook().getIsbn())")
    @Mapping(target = "readerNumber",expression = "java(lending.getReaderDetails().getReaderNumber())")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "limitDate", source = "limitDate")
    @Mapping(target = "fineValuePerDayInCents", expression = "java(lending.getFineValuePerDayInCents())")
    @Mapping(target = "commentary", source = "commentary")
    @Mapping(target = "version", source = "version")

    public abstract LendingViewAMQP toLendingViewAMQP(Lending lending);

    public abstract List<LendingViewAMQP> toLendingViewAMQP(List<Lending> lendingList);
}
