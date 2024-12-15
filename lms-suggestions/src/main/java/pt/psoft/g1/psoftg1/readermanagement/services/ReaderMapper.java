package pt.psoft.g1.psoftg1.readermanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

/**
 * Brief guide:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * */
@Mapper(componentModel = "spring", implementationName = "CustomReaderMapperImpl")
public abstract class ReaderMapper {

    @Mapping(target = "readerNumber", source = "readerNumber")
    public abstract ReaderDetails createReaderDetails(CreateReaderRequest request);
}
