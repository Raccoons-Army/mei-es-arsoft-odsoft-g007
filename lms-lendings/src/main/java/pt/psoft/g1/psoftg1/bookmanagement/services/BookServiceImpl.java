package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.stereotype.Service;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public void create(BookViewAMQP request) {
        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new ConflictException("Book with ISBN " + request.getIsbn() + " already exists");
        }
        Book book = new Book(request.getIsbn());
        bookRepository.save(book);
    }
}
