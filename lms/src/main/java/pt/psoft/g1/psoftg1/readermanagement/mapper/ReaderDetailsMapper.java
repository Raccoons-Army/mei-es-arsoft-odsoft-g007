package pt.psoft.g1.psoftg1.readermanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.mapper.GenreMapper;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.JpaReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.dbSchema.MongoReaderDetailsModel;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.mapper.PhotoMapper;
import pt.psoft.g1.psoftg1.usermanagement.mapper.ReaderMapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {ReaderMapper.class, PhotoMapper.class, GenreMapper.class})
public abstract class ReaderDetailsMapper {

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    @Mapping(target = "thirdPartySharingConsent", source = "thirdPartySharingConsent")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "interestList", source = "interestList")
    @Mapping(target = "photoURI", source = "photo")
    public abstract JpaReaderDetailsModel toJpaReaderDetailsModel(ReaderDetails readerDetails);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    @Mapping(target = "thirdPartySharingConsent", source = "thirdPartySharingConsent")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "interestList", source = "interestList")
    @Mapping(target = "photoURI", source = "photo")
    public abstract ReaderDetails fromJpaReaderDetailsModel(JpaReaderDetailsModel jpaReaderDetailsModel);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    @Mapping(target = "thirdPartySharingConsent", source = "thirdPartySharingConsent")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "interestList", source = "interestList")
    @Mapping(target = "photoURI", source = "photo")
    public abstract MongoReaderDetailsModel toMongoReaderDetailsModel(ReaderDetails readerDetails);

    @Mapping(target = "pk", source = "pk")
    @Mapping(target = "reader", source = "reader")
    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    @Mapping(target = "thirdPartySharingConsent", source = "thirdPartySharingConsent")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "interestList", source = "interestList")
    @Mapping(target = "photoURI", source = "photo")
    public abstract ReaderDetails fromMongoReaderDetailsModel(MongoReaderDetailsModel mongoReaderDetailsModel);

    public BirthDate map(String birthDate) {
        return new BirthDate(birthDate);
    }

    public LocalDate map(BirthDate birthDate) {
        return birthDate != null ? birthDate.getBirthDate() : null;
    }
}
