package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Getter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.FactoryAuthor;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.FactoryGenre;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book extends EntityWithPhoto {

    @Getter
    long pk;

    @Getter
    private Long version;

    Isbn isbn;

    @Getter
    Title title;

    @Getter
    Genre genre;

    @Getter
    private List<Author> authors = new ArrayList<>();

    Description description;

    FactoryGenre _factoryGenre;
    FactoryAuthor _factoryAuthor;

    private void setTitle(String title) {this.title = new Title(title);}

    private void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    private void setDescription(String description) {this.description = new Description(description); }

    private void setGenre(Genre genre) {this.genre = genre; }

    private void setAuthors(List<Author> authors) {this.authors = authors; }

    public String getDescription(){ return this.description.toString(); }

    public Book(String isbn, String title, String description, String photoURI, FactoryGenre factoryGenre, FactoryAuthor factoryAuthor) {
        setTitle(title);
        setIsbn(isbn);
        if(description != null)
            setDescription(description);
        setPhotoInternal(photoURI);
        _factoryGenre = factoryGenre;
        _factoryAuthor = factoryAuthor;
    }

    protected Book() {
        // got ORM only
    }

    public Genre defineGenre(String name) throws InstantiationException {
        this.genre = _factoryGenre.newGenre(name);
        return this.genre;
    }

    public Author addAuthor(String authorNumber, String name, String bio, String photoURI) throws InstantiationException {
        Author author = _factoryAuthor.newAuthor(authorNumber, name, bio, photoURI);
        this.authors.add(author);
        return author;
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }

    public void applyPatch(final Long desiredVersion, UpdateBookRequest request) {
        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        String title = request.getTitle();
        String description = request.getDescription();
        Genre genre = request.getGenreObj();
        List<Author> authors = request.getAuthorObjList();
        String photoURI = request.getPhotoURI();
        if(title != null) {
            setTitle(title);
        }

        if(description != null) {
            setDescription(description);
        }

        if(genre != null) {
            setGenre(genre);
        }

        if(authors != null) {
            setAuthors(authors);
        }

        if(photoURI != null)
            setPhotoInternal(photoURI);

    }

    public String getIsbn(){
        return this.isbn.toString();
    }

}
