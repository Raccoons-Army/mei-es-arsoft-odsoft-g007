package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;

	@Value("${suggestionsLimitPerGenre}")
	private long suggestionsLimitPerGenre;

	@Override
	public void create(CreateBookRequest request) {

		if(bookRepository.findByIsbn(request.getIsbn()).isPresent()){
			return;
		}

		Book book = bookMapper.createBook(request);
		bookRepository.save(book);
	}

	public Book findByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new NotFoundException(Book.class, isbn));
	}

}
