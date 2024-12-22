package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CRUDRepository<Book, String> {
    List<Book> findByGenre(String genre);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findBooksByAuthorNumber(String authorNumber);
    Page<BookCountDTO> findTopXBooksLent(Pageable pageable);
}
