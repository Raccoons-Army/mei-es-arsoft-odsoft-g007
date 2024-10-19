package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends CRUDRepository<Book, Isbn> {

    List<Book> findByGenre(String genre);
    List<Book> findByTitle(String title);
    List<Book> findByAuthorName(String authorName);
    Optional<Book> findByIsbn(String isbn);
    Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable);
    List<Book> findBooksByAuthorNumber(String authorNumber);
    List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query);
}
