package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.time.LocalDate;
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

	@Override
	public Book create(BookViewAMQP bookViewAMQP) {
		final String isbn = bookViewAMQP.getIsbn();
		final String description = bookViewAMQP.getDescription();
		final String title = bookViewAMQP.getTitle();
		final String genre = bookViewAMQP.getGenre();
		final List<String> authorIds = bookViewAMQP.getAuthorIds();

        return create(isbn, title, description, genre, authorIds);
	}

	private Book create( String isbn,
						 String title,
						 String description,
						 String genreName,
						 List<String> authorIds) {

		if (bookRepository.findByIsbn(isbn).isPresent()) {
			throw new ConflictException("Book with ISBN " + isbn + " already exists");
		}

		List<Author> authors = getAuthors(authorIds);

		final Genre genre = genreRepository.findByString(String.valueOf(genreName))
				.orElseThrow(() -> new NotFoundException("Genre not found"));

		Book newBook = new Book(isbn, title, description, genre, authors);

        return bookRepository.save(newBook);
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

		book.applyPatch(currentVersion, title, description, genreObj, authors);

		Book updatedBook = bookRepository.save(book);

		return updatedBook;
	}

	@Override
	public List<Book> findByGenre(String genre) {
		return this.bookRepository.findByGenre(genre);
	}

	@Override
	public Book findByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new NotFoundException(Book.class, isbn));
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
