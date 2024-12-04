package pt.psoft.g1.psoftg1.readermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;
import pt.psoft.g1.psoftg1.shared.util.DateUtils;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderViewAMQPMapper extends MapperInterface {

    @Mapping(target = "version", source = "version")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "birthDate", expression = "java(formatLocalDate(reader.getBirthDate().getBirthDate()))")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "photo", source = "photo")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    @Mapping(target = "thirdPartySharingConsent", source = "thirdPartySharingConsent")
    @Mapping(target = "interestList", source = "interestList")

    public abstract ReaderViewAMQP toReaderViewAMQP(ReaderDetails reader);
    public abstract List<ReaderViewAMQP> toReaderViewAMQP(List<ReaderDetails> readerList);

    // Utility method to format LocalDate -> String
    protected String formatLocalDate(LocalDate date) {
        return date != null ? DateUtils.ISO_DATE_FORMATTER.format(date) : null;
    }
}
