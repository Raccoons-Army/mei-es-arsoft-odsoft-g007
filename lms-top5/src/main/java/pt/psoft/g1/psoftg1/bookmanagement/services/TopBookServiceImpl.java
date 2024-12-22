package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.TopBook;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.TopBookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class TopBookServiceImpl implements TopBookService {

    @Value("${topBooks}")
    private int topBooksLimit;

    private final BookRepository bookRepository;
    private final TopBookRepository topBookRepository;

    @Override
    public List<TopBook> findTopX() {
        return topBookRepository.findAll();
    }

    @Override
    @Transactional
    public void updateTopX() {
        Pageable pageableRules = PageRequest.of(0, topBooksLimit);
        // fetch top 5 most lent books
        List<BookCountDTO> topBooks = bookRepository.findTopXBooksLent(pageableRules).getContent();

        if(topBooks.isEmpty()) {
            return;
        }

        // Clear existing records
        topBookRepository.deleteAll();

        // Save new top books
        for (BookCountDTO e : topBooks) {
            Book b = e.getBook();
            List<String> authorsNames = b.getAuthors().stream().map(Author::getName).toList(); // get authors names
            TopBook topBook = new TopBook(b.getIsbn(), b.getTitle(), b.getGenre().getGenre(),
                    b.getDescription(), authorsNames, e.getLendingCount());
            topBookRepository.save(topBook);
        }
    }

}
