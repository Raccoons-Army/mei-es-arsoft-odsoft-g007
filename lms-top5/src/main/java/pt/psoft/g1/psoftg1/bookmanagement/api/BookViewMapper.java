package pt.psoft.g1.psoftg1.bookmanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class BookViewMapper extends MapperInterface {
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "lendingCount", source = "lendingCount")
    @Mapping(target = "_links", expression = "java(mapLinks(book.getIsbn(), book.getAuthors()))")
    public abstract BookView toBookView(TopBook book);
    public abstract List<BookView> toBookView(List<TopBook> bookList);

    protected List<String> mapAuthors(List<Author> authors) {
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.toList());
    }

    @Named(value = "mapBookLinks")
    public Map<String, Object> mapLinks(final String isbn, final List<String> authors) {
        String bookUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/books/")
                .path(isbn)
                .toUriString();

        Map<String, Object> links = new HashMap<>();
        links.put("self", bookUri);

        List<Map<String, String>> authorLinks = authors.stream()
                .map(author -> {
                    String authorUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/authors/")
                            .path(author)
                            .toUriString();
                    Map<String, String> authorLink = new HashMap<>();
                    authorLink.put("href", authorUri);
                    return authorLink;
                })
                .collect(Collectors.toList());

        links.put("authors", authorLinks);

        return links;
    }
}
