package pt.psoft.g1.psoftg1.bookmanagement.api;

import org.checkerframework.checker.units.qual.A;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BookViewAMQPMapper extends MapperInterface {

    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "authorIds", expression = "java(mapAuthors(book.getAuthors()))")
    @Mapping(target = "version", source = "version")

    public abstract BookViewAMQP toBookViewAMQP(Book book);

    public abstract List<BookViewAMQP> toBookViewAMQP(List<Book> bookList);


    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "genre", expression = "java(mapGenre(bookViewAMQP.getGenre()))")
    @Mapping(target = "authors", expression = "java(mapAuthorsIds(bookViewAMQP.getAuthorIds()))")
    @Mapping(target = "version", source = "version")

    public abstract Book toBook(BookViewAMQP bookViewAMQP);

    public abstract List<Book> toBook(List<BookViewAMQP> bookViewAMQPs);

    protected List<String> mapAuthors(List<Author> authors) {
        return authors.stream().map(Author::getAuthorNumber).collect(Collectors.toList());
    }

    protected List<Author> mapAuthorsIds(List<String> authorIds) {
        List<Author> authors = new ArrayList<>();
        for(String id : authorIds) {
            Author author =  new Author();
            author.setAuthorNumber(id);
            authors.add(author);
        }
        return authors;
    }

    protected Genre mapGenre(String genre) {
        return new Genre(genre);
    }
}
