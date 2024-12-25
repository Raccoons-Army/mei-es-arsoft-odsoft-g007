package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class Book {

    private Long version;

    Isbn isbn;

    Title title;

    @Setter
    Genre genre;

    @Setter
    private List<Author> authors = new ArrayList<>();

    Description description;

    FactoryGenre _factoryGenre;

    FactoryAuthor _factoryAuthor;

    public void setTitle(String title) {this.title = new Title(title);}

    public void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    public void setDescription(String description) {this.description = new Description(description); }

    public String getDescription(){ return this.description.toString(); }

    public Book(String isbn, String title, String description, Genre genre, List<Author> authors) {
        setTitle(title);
        setIsbn(isbn);
        if(description != null)
            setDescription(description);
        setGenre(genre);
        setAuthors(authors);
    }

    public Book(String isbn, String title, String description, FactoryGenre factoryGenre, FactoryAuthor factoryAuthor) {
        setTitle(title);
        setIsbn(isbn);
        if(description != null)
            setDescription(description);
        _factoryGenre = factoryGenre;
        _factoryAuthor = factoryAuthor;
    }

    public Book() {
        // got ORM only
    }

    public void defineGenre(String pk, String name) throws InstantiationException {
        this.genre = _factoryGenre.newGenre(pk, name);
    }

    public void defineGenre(String name) throws InstantiationException {
        this.genre = _factoryGenre.newGenre(name);
    }

    public Author addAuthor(String authorNumber, String name) throws InstantiationException {
        Author author = _factoryAuthor.newAuthor(authorNumber, name);
        this.authors.add(author);
        return author;
    }

    public Author addAuthor(String authorNumber, String name, long version) {
        Author author = _factoryAuthor.newAuthor(authorNumber, name, version);
        this.authors.add(author);
        return author;
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }
    }

    public void applyPatch(final Long desiredVersion,
                           final String title,
                           final String description,
                           final Genre genre,
                           final List<Author> authors ) {

        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.isbn);

        if (title != null) {
            setTitle(title);
        }

        if (description != null) {
            setDescription(description);
        }

        if (genre != null) {
            setGenre(genre);
        }

        if (authors != null) {
            setAuthors(authors);
        }
    }

    public String getIsbn(){
        return this.isbn.toString();
    }

    public String getTitle(){
        return this.title.title;
    }
}
