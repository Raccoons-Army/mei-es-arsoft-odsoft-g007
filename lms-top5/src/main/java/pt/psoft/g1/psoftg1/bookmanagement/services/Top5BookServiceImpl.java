package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.Top5BookRepository;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Top5BookServiceImpl implements Top5BookService {

    private final BookRepository bookRepository;
    private final Top5BookRepository top5BookRepository;

    @Override
    public List<TopBook> findTop5BooksLent() {
        return top5BookRepository.findAll();
    }

    @Override
    @Transactional
    public void updateTop5BooksLent() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        Pageable pageableRules = PageRequest.of(0, 5);
        // fetch top 5 most lent books
        List<BookCountDTO> topBooks = bookRepository.findTop5BooksLent(oneYearAgo, pageableRules).getContent();

        // Clear existing records
        top5BookRepository.deleteAll();

        // Save new top books
        for (BookCountDTO e : topBooks) {
            Book b = e.getBook();
            List<String> authorsNames = b.getAuthors().stream().map(Author::getName).toList(); // get authors names
            TopBook topBook = new TopBook(b.getIsbn(), b.getTitle(), b.getGenre().getGenre(),
                    b.getDescription(), authorsNames, e.getLendingCount());
            top5BookRepository.save(topBook);
        }
    }

}
