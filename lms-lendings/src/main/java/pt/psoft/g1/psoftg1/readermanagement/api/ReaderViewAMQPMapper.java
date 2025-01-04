package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingViewAMQP;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.shared.util.DateUtils;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewAMQPMapper extends MapperInterface {

    @Mapping(target = "readerNumber", expression = "java(reader.getReaderNumber())")
    @Mapping(target = "readerUsername", source = "reader.reader.username")
    @Mapping(target = "birthDate", ignore = true) 
    @Mapping(target = "phoneNumber", ignore = true) 
    @Mapping(target = "gdprConsent", ignore = true) 
    @Mapping(target = "marketingConsent", ignore = true) 
    @Mapping(target = "thirdPartySharingConsent", ignore = true) 
    @Mapping(target = "interestList", ignore = true) 
    @Mapping(target = "version", source = "version")
    public abstract ReaderViewAMQP toReaderViewAMQP(ReaderDetails reader);

    public abstract List<ReaderViewAMQP> toReaderViewAMQP(List<ReaderDetails> readerList);

    @Mapping(target = "readerNumber", expression = "java(new ReaderNumber(readerViewAMQP.getReaderNumber()))")
    @Mapping(target = "reader.username", source = "readerUsername")
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetails toReaderDetails(ReaderViewAMQP readerViewAMQP);

    public abstract List<ReaderDetails> toReaderDetails(List<ReaderViewAMQP> readerViewAMQPList);
}
