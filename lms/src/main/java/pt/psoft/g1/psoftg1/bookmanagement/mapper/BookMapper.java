package pt.psoft.g1.psoftg1.bookmanagement.mapper;

import pt.psoft.g1.psoftg1.authormanagement.dbSchema.JpaAuthorModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.JpaBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.dbSchema.MongoBookModel;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookId;
import pt.psoft.g1.psoftg1.genremanagement.dbSchema.JpaGenreModel;

import java.util.List;
import java.util.function.Function;

public class BookMapper {

    
    public static Book fromJpaToDomain(JpaBookModel jpaBookModel) {
        if (jpaBookModel == null) {
            return null;
        }

        // Extract authors ids from authors
        List<Long> authorsIds = extractEntitiesIds(jpaBookModel.getAuthors(), JpaAuthorModel::getAuthorNumber);

        return new Book(new BookId(jpaBookModel.getId()), jpaBookModel.getIsbn(), jpaBookModel.getTitle(), jpaBookModel.getDescription(),
                jpaBookModel.getGenre().getId(), authorsIds, null);
    }

    
    public static Book fromMongoToDomain(MongoBookModel mongoBookModel) {
        if (mongoBookModel == null) {
            return null;
        }

        return new Book(new BookId(mongoBookModel.getId()), mongoBookModel.getIsbn(), mongoBookModel.getTitle(), mongoBookModel.getDescription(),
                mongoBookModel.getGenre(), mongoBookModel.getAuthors(), null);
    }

    
    public static JpaBookModel fromDomainToJpa(Book book) {
        if (book == null) {
            return null;
        }

        // Extract authors from book
        List<JpaAuthorModel> authors = extractEntities(book.getAuthors(), JpaAuthorModel::new);

        return new JpaBookModel(book.getId().toString(), book.getVersion(), book.getIsbn(), book.getTitle().toString(),
                new JpaGenreModel(book.getGenre()), authors, book.getDescription());
    }

    
    public static MongoBookModel fromDomainToMongo(Book book) {
        if (book == null) {
            return null;
        }

        return new MongoBookModel(book.getId().toString(), book.getIsbn(), book.getTitle().toString(),
                book.getGenre(), book.getAuthors(), book.getDescription());
    }

    // Extract, for example, authors ids from Author db schema
    private static <T> List<Long> extractEntitiesIds(List<T> authors, Function<T, Long> idExtractor) {
        return authors.stream()
                .map(idExtractor)
                .toList();
    }

    // Extract, for example, authors from authors ids (it creates a Author db schema with only the id)
    private static <T> List<T> extractEntities(List<Long> authorsIds, Function<Long, T> authorExtractor) {
        return authorsIds.stream()
                .map(authorExtractor)
                .toList();
    }
}
