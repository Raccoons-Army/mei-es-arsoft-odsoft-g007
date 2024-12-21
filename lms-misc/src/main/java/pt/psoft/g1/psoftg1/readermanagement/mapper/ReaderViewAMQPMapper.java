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
    public abstract ReaderViewAMQP toReaderViewAMQP(ReaderDetails reader);
    public abstract List<ReaderViewAMQP> toReaderViewAMQP(List<ReaderDetails> readerList);

    // Utility method to format LocalDate -> String
    protected String formatLocalDate(LocalDate date) {
        return date != null ? DateUtils.ISO_DATE_FORMATTER.format(date) : null;
    }
}
