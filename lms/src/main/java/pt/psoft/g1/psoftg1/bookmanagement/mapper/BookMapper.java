package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorDTO;
import pt.psoft.g1.psoftg1.authormanagement.dbSchema.MongoAuthorDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.mapper.Mapper;

import java.util.List;
import java.util.function.Function;

public class BookMapper implements Mapper<JpaBookDTO, MongoBookDTO, Book> {

    @Override
    public Book fromJpaToDomain(JpaBookDTO jpaBookDTO) {
        if (jpaBookDTO == null) {
            return null;
        }

        // Extract authors ids from authors
        List<Long> authorsIds = extractAuthorsIds(jpaBookDTO.getAuthors(), JpaAuthorDTO::getAuthorNumber);

        return new Book(jpaBookDTO.getIsbn(), jpaBookDTO.getTitle(), jpaBookDTO.getDescription(),
                jpaBookDTO.getGenre(), authorsIds, null);
    }

    @Override
    public Book fromMongoToDomain(MongoBookDTO mongoBookDTO) {
        if (mongoBookDTO == null) {
            return null;
        }

        // Extract authors ids from authors
        List<Long> authorsIds = extractAuthorsIds(mongoBookDTO.getAuthors(), MongoAuthorDTO::getAuthorNumber);

        return new Book(mongoBookDTO.getIsbn(), mongoBookDTO.getTitle(), mongoBookDTO.getDescription(),
                mongoBookDTO.getGenre(), authorsIds, null);
    }

    @Override
    public JpaBookDTO fromDomainToJpa(Book book) {
        if (book == null) {
            return null;
        }

        // Extract authors from book
        List<JpaAuthorDTO> authors = extractAuthors(book.getAuthors(), JpaAuthorDTO::new);

        return new JpaBookDTO(book.getPk(), book.getVersion(), book.getIsbn(), book.getTitle().toString(),
                book.getGenre(), authors, book.getDescription());
    }

    @Override
    public MongoBookDTO fromDomainToMongo(Book book) {
        if (book == null) {
            return null;
        }

        // Extract authors from book
        List<MongoAuthorDTO> authors = extractAuthors(book.getAuthors(), MongoAuthorDTO::new);

        return new MongoBookDTO(book.getPk(), book.getIsbn(), book.getTitle().toString(),
                book.getGenre(), authors, book.getDescription());
    }

    // Extract authors ids from authors
    private <T> List<Long> extractAuthorsIds(List<T> authors, Function<T, Long> idExtractor) {
        return authors.stream()
                .map(idExtractor)
                .toList();
    }

    // Extract authors from authors ids
    private <T> List<T> extractAuthors(List<Long> authorsIds, Function<Long, T> authorExtractor) {
        return authorsIds.stream()
                .map(authorExtractor)
                .toList();
    }
}
