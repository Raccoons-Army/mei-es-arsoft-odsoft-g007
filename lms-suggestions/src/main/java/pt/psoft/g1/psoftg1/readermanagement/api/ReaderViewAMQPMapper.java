package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewAMQPMapper extends MapperInterface {

    @Mapping(target = "readerNumber", expression = "java(reader.getReaderNumber())")
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true) 
    @Mapping(target = "gdprConsent", ignore = true) 
    @Mapping(target = "marketingConsent", ignore = true) 
    @Mapping(target = "thirdPartySharingConsent", ignore = true) 
    @Mapping(target = "interestList", ignore = true) 
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetailsViewAMQP toReaderViewAMQP(ReaderDetails reader);
    public abstract List<ReaderDetailsViewAMQP> toReaderViewAMQP(List<ReaderDetails> readerList);

    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "version", source = "version")
    public abstract ReaderDetails toReaderDetails(ReaderDetailsViewAMQP readerViewAMQP);

    public abstract List<ReaderDetails> toReaderDetails(List<ReaderDetailsViewAMQP> readerViewAMQPList);
}
