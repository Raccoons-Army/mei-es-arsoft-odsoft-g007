package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.shared.mapper.Mapper;

public class BookMapper implements Mapper<JpaBookDTO, MongoBookDTO, Book> {

    @Override
    public Book fromJpaToDomain(JpaBookDTO jpaBookDTO) {
        if (jpaBookDTO == null) {
            return null;
        }

        return new Book(jpaBookDTO.getIsbn(), jpaBookDTO.getTitle(), jpaBookDTO.getDescription(),
                jpaBookDTO.getGenre(), jpaBookDTO.getAuthors(), null);
    }

    @Override
    public Book fromMongoToDomain(MongoBookDTO mongoBookDTO) {
        if (mongoBookDTO == null) {
            return null;
        }

        return new Book(mongoBookDTO.getIsbn(), mongoBookDTO.getTitle(), mongoBookDTO.getDescription(),
                mongoBookDTO.getGenre(), mongoBookDTO.getAuthors(), null);
    }

    @Override
    public JpaBookDTO fromDomainToJpa(Book book) {
        if (book == null) {
            return null;
        }

        return new JpaBookDTO(book.getPk(), book.getVersion(), book.getIsbn(), book.getTitle().toString(),
                book.getGenre(), book.getAuthors(), book.getDescription());
    }

    @Override
    public MongoBookDTO fromDomainToMongo(Book book) {
        if (book == null) {
            return null;
        }

        return new MongoBookDTO(book.getPk(), book.getIsbn(), book.getTitle().toString(),
                book.getGenre(), book.getAuthors(), book.getDescription());
    }
}
