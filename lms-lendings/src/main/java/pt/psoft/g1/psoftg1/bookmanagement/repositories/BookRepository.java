package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.shared.repositories.CRUDRepository;

import java.util.Optional;

@Repository
public interface BookRepository extends CRUDRepository<Book, String> {
    Optional<Book> findByIsbn(String isbn);
}
