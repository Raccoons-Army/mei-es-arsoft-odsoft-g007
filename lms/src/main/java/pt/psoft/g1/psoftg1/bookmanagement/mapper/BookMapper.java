package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.mapper.Mapper;

public class BookMapper implements Mapper<JpaBookDTO, MongoBookDTO, Book> {

    @Override
    public Book fromJpaToDomain(JpaBookDTO jpaBookDTO) {
        return null;
    }

    @Override
    public Book fromMongoToDomain(MongoBookDTO mongoBookDTO) {
        return null;
    }

    @Override
    public JpaBookDTO fromDomainToJpa(Book book) {
        return null;
    }

    @Override
    public MongoBookDTO fromDomainToMongo(Book book) {
        return null;
    }
}
