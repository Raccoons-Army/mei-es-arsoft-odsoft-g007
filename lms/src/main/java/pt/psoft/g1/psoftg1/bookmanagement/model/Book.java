package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Getter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
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
    private List<Long> authors = new ArrayList<>();

    Description description;

    private void setTitle(String title) {this.title = new Title(title);}

    private void setIsbn(String isbn) {
        this.isbn = new Isbn(isbn);
    }

    private void setDescription(String description) {this.description = new Description(description); }

    private void setGenre(Genre genre) {this.genre = genre; }

    private void setAuthors(List<Long> authors) {this.authors = authors; }

    public String getDescription(){ return this.description.toString(); }

    public Book(String isbn, String title, String description, Genre genre, List<Long> authors, String photoURI) {
        setTitle(title);
        setIsbn(isbn);
        if(description != null)
            setDescription(description);
        if(genre==null)
            throw new IllegalArgumentException("Genre cannot be null");
        setGenre(genre);
        if(authors == null)
            throw new IllegalArgumentException("Author list is null");
        if(authors.isEmpty())
            throw new IllegalArgumentException("Author list is empty");

        setAuthors(authors);
        setPhotoInternal(photoURI);
    }

    protected Book() {
        // got ORM only
    }

    public void removePhoto(long desiredVersion) {
        if(desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }

    public void applyPatch(Long desiredVersion, UpdateBookRequest request) {
        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        String title = request.getTitle();
        String description = request.getDescription();
        Genre genre = request.getGenreObj();
        List<Long> authors = request.getAuthorObjList();
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
