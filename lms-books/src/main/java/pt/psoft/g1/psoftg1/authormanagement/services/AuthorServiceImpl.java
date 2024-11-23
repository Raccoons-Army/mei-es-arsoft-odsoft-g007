package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewAMQP;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.publishers.AuthorEventsPublisher;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper mapper;
    private final PhotoRepository photoRepository;
    private final IdGenerationStrategy<String> idGenerationStrategy;

    private final AuthorEventsPublisher authorEventsPublisher;

    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findByAuthorNumber(final String authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.searchByNameNameStartsWith(name);
    }

    @Override
    public Author create(final CreateAuthorRequest resource) {
        resource.setPhoto(null);
        resource.setPhotoURI(null);
        final Author author = mapper.create(idGenerationStrategy.generateId(), resource);
        Author savedAuthor = authorRepository.save(author);
        if (savedAuthor != null) {
            authorEventsPublisher.sendAuthorCreated(savedAuthor);
        }
        return savedAuthor;
    }

    @Override
    public Author create(AuthorViewAMQP resource) {
        // check if already exists
        if (authorRepository.findByAuthorNumber(resource.getAuthorNumber()).isPresent()) {
            throw new ConflictException("Author with number " + resource.getAuthorNumber() + " already exists");
        }
        final Author author = new Author(resource.getAuthorNumber(), resource.getName(), resource.getBio(), null);
        return authorRepository.save(author);
    }

    @Override
    public Author update(AuthorViewAMQP resource) {
        final Author author = authorRepository.findByAuthorNumber(resource.getAuthorNumber())
                .orElseThrow(() -> new NotFoundException("Cannot find author"));
        author.setName(resource.getName());
        author.setBio(resource.getBio());
        return authorRepository.save(author);
    }

    @Override
    public Author partialUpdate(final String authorNumber, final UpdateAuthorRequest request, final long desiredVersion) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var author = findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));
        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }
        // since we got the object from the database we can check the version in memory
        // and apply the patch

        author.applyPatch(desiredVersion, request);

        // in the meantime some other user might have changed this object on the
        // database, so concurrency control will still be applied when we try to save
        // this updated object
        return authorRepository.save(author);
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        return bookRepository.findBooksByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findCoAuthorsByAuthorNumber(String authorNumber) {
        return authorRepository.findCoAuthorsByAuthorNumber(authorNumber);
    }

    @Override
    public Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion) {
        Author author = authorRepository.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = author.getPhoto().getPhotoFile();
        author.removePhoto(desiredVersion);
        Optional<Author> updatedAuthor = Optional.of(authorRepository.save(author));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedAuthor;
    }

}

