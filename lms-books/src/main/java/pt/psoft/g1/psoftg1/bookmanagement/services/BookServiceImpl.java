package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final GenreRepository genreRepository;
	private final AuthorRepository authorRepository;
	private final PhotoRepository photoRepository;

	private final BookEventsPublisher bookEventsPublisher;

	@Override
	public Book create(CreateBookRequest request, String isbn) {

		final String title = request.getTitle();
		final String description = request.getDescription();
		final String photoURI = request.getPhotoURI();
		final String genre = request.getGenre();
		final List<String> authorIds = request.getAuthors();

		Book savedBook = create(isbn, title, description, photoURI, genre, authorIds);

		if( savedBook!=null ) {
			bookEventsPublisher.sendBookCreated(savedBook);
		}

		return savedBook;
	}

	@Override
	public Book create(BookViewAMQP bookViewAMQP) {
		final String isbn = bookViewAMQP.getIsbn();
		final String description = bookViewAMQP.getDescription();
		final String title = bookViewAMQP.getTitle();
		final String photoURI = null;
		final String genre = bookViewAMQP.getGenre();
		final List<String> authorIds = bookViewAMQP.getAuthorIds();

        return create(isbn, title, description, photoURI, genre, authorIds);
	}

	private Book create( String isbn,
						 String title,
						 String description,
						 String photoURI,
						 String genreName,
						 List<String> authorIds) {

		if (bookRepository.findByIsbn(isbn).isPresent()) {
			throw new ConflictException("Book with ISBN " + isbn + " already exists");
		}

		List<Author> authors = getAuthors(authorIds);

		final Genre genre = genreRepository.findByString(String.valueOf(genreName))
				.orElseThrow(() -> new NotFoundException("Genre not found"));

		Book newBook = new Book(isbn, title, description, photoURI, genre, authors);

        return bookRepository.save(newBook);
	}


	@Override
	public Book update(UpdateBookRequest request, String currentVersion) {

		Long currentVersionParsed = Long.parseLong(currentVersion);

		var book = findByIsbn(request.getIsbn());
		List<String> authorsId = request.getAuthors();

		MultipartFile photo = request.getPhoto();
		String photoURI = request.getPhotoURI();
		if (photo == null && photoURI != null || photo != null && photoURI == null) {
			photoURI = null;
		}

		String genreId = request.getGenre();
		String title = request.getTitle();
		String description = request.getDescription();

		Book updatedBook = update( book, currentVersionParsed, title, description, photoURI, genreId, authorsId);
		if( updatedBook!=null ) {
			bookEventsPublisher.sendBookUpdated(updatedBook, currentVersionParsed);
		}

		return updatedBook;
	}

	@Override
	public Book update(BookViewAMQP bookViewAMQP) {

		final Long version = bookViewAMQP.getVersion();
		final String isbn = bookViewAMQP.getIsbn();
		final String description = bookViewAMQP.getDescription();
		final String title = bookViewAMQP.getTitle();
		final String photoURI = null;
		final String genre = bookViewAMQP.getGenre();
		final List<String> authorIds = bookViewAMQP.getAuthorIds();

		var book = findByIsbn(isbn);

		Book bookUpdated = update(book, version, title, description, photoURI, genre, authorIds);

		return bookUpdated;
	}

	private Book update( Book book,
						 Long currentVersion,
						 String title,
						 String description,
						 String photoURI,
						 String genreId,
						 List<String> authorsId) {

		Genre genreObj = null;
		if (genreId != null) {
			Optional<Genre> genre = genreRepository.findByString(genreId);
			if (genre.isEmpty()) {
				throw new NotFoundException("Genre not found");
			}
			genreObj = genre.get();
		}

		List<Author> authors = new ArrayList<>();
		if (authorsId != null) {
			for (String authorNumber : authorsId) {
				Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
				if (temp.isEmpty()) {
					continue;
				}
				Author author = temp.get();
				authors.add(author);
			}
		}
		else
			authors = null;

		book.applyPatch(currentVersion, title, description, photoURI, genreObj, authors);

		Book updatedBook = bookRepository.save(book);

		return updatedBook;
	}


	@Override
	public Book save(Book book) {
		return this.bookRepository.save(book);
	}

	@Override
	public Book removeBookPhoto(String isbn, long desiredVersion) {
		Book book = this.findByIsbn(isbn);
		String photoFile;
		try {
			photoFile = book.getPhoto().getPhotoFile();
		}catch (NullPointerException e){
			throw new NotFoundException("Book did not have a photo assigned to it.");
		}

		book.removePhoto(desiredVersion);
		var updatedBook = bookRepository.save(book);
		photoRepository.deleteByPhotoFile(photoFile);
		return updatedBook;
	}

	@Override
	public List<Book> findByGenre(String genre) {
		return this.bookRepository.findByGenre(genre);
	}

	public List<Book> findByTitle(String title) {
		return bookRepository.findByTitle(title);
	}

	@Override
	public List<Book> findByAuthorName(String authorName) {
		return bookRepository.findByAuthorName(authorName + "%");
	}

	public Book findByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new NotFoundException(Book.class, isbn));
	}

	@Override
		public List<Book> searchBooks(Page page, SearchBooksQuery query) {
		if (page == null) {
			page = new Page(1, 10);
		}
		if (query == null) {
			query = new SearchBooksQuery("", "", "");
		}
		return bookRepository.searchBooks(page, query);
	}


	private List<Author> getAuthors(List<String> authorNumbers) {

		List<Author> authors = new ArrayList<>();
		for (String authorNumber : authorNumbers) {

			Optional<Author> temp = authorRepository.findByAuthorNumber(authorNumber);
			if (temp.isEmpty()) {
				continue;
			}

			Author author = temp.get();
			authors.add(author);
		}

		return authors;
	}
}
