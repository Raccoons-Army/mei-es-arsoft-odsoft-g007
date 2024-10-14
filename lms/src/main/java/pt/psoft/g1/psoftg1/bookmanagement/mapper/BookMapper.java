package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookDTO;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookId;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreDTO;
import pt.psoft.g1.psoftg1.shared.mapper.Mapper;

import java.util.List;
import java.util.function.Function;

public class BookMapper implements Mapper<JpaBookDTO, MongoBookDTO, Book> {

    @Override
    public static Book fromJpaToDomain(JpaBookDTO jpaBookDTO) {
        if (jpaBookDTO == null) {
            return null;
        }

        // Extract authors ids from authors
        List<Long> authorsIds = extractEntitiesIds(jpaBookDTO.getAuthors(), JpaAuthorDTO::getAuthorNumber);

        return new Book(new BookId(jpaBookDTO.getId()), jpaBookDTO.getIsbn(), jpaBookDTO.getTitle(), jpaBookDTO.getDescription(),
                jpaBookDTO.getGenre().getId(), authorsIds, null);
    }

    @Override
    public Book fromMongoToDomain(MongoBookDTO mongoBookDTO) {
        if (mongoBookDTO == null) {
            return null;
        }

        return new Book(new BookId(mongoBookDTO.getId()), mongoBookDTO.getIsbn(), mongoBookDTO.getTitle(), mongoBookDTO.getDescription(),
                mongoBookDTO.getGenre(), mongoBookDTO.getAuthors(), null);
    }

    @Override
    public JpaBookDTO fromDomainToJpa(Book book) {
        if (book == null) {
            return null;
        }

        // Extract authors from book
        List<JpaAuthorDTO> authors = extractEntities(book.getAuthors(), JpaAuthorDTO::new);

        return new JpaBookDTO(book.getId().toString(), book.getVersion(), book.getIsbn(), book.getTitle().toString(),
                new JpaGenreDTO(book.getGenre()), authors, book.getDescription());
    }

    @Override
    public MongoBookDTO fromDomainToMongo(Book book) {
        if (book == null) {
            return null;
        }

        return new MongoBookDTO(book.getId().toString(), book.getIsbn(), book.getTitle().toString(),
                book.getGenre(), book.getAuthors(), book.getDescription());
    }

    // Extract, for example, authors ids from Author db schema
    private <T> List<Long> extractEntitiesIds(List<T> authors, Function<T, Long> idExtractor) {
        return authors.stream()
                .map(idExtractor)
                .toList();
    }

    // Extract, for example, authors from authors ids (it creates a Author db schema with only the id)
    private <T> List<T> extractEntities(List<Long> authorsIds, Function<Long, T> authorExtractor) {
        return authorsIds.stream()
                .map(authorExtractor)
                .toList();
    }
}
