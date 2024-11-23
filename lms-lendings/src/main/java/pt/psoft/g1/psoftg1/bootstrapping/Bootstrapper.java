package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({"classpath:config/library.properties"})
@Order(2)
public class Bootstrapper implements CommandLineRunner {
    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;
    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    private final BookRepository bookRepository;
    private final LendingRepository lendingRepository;
    private final ReaderRepository readerRepository;
    private final IdGenerationStrategy<String> idGenerationStrategy;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createBooks();
        createLendings();
    }

    protected void createBooks() {
        // 1 - O País das Pessoas de Pernas Para o Ar
        if (bookRepository.findByIsbn("9789720706386").isEmpty()) {
            Book book = new Book("9789720706386");
            bookRepository.save(book);
        }

        // 2 - Como se Desenha Uma Casa
        if (bookRepository.findByIsbn("9789723716160").isEmpty()) {
            Book book = new Book("9789723716160");
            bookRepository.save(book);
        }

        // 3 - C e Algoritmos
        if (bookRepository.findByIsbn("9789895612864").isEmpty()) {
            Book book = new Book("9789895612864");
            bookRepository.save(book);
        }


        // 4 - Introdução ao Desenvolvimento Moderno para a Web
        if (bookRepository.findByIsbn("9782722203402").isEmpty()) {
            Book book = new Book("9782722203402");
            bookRepository.save(book);
        }

        // 5 - O Principezinho
        if (bookRepository.findByIsbn("9789722328296").isEmpty()) {
            Book book = new Book("9789722328296");
            bookRepository.save(book);
        }

        // 6 - A Criada Está a Ver
        if (bookRepository.findByIsbn("9789895702756").isEmpty()) {
            Book book = new Book("9789895702756");
            bookRepository.save(book);
        }

        // 7 - O Hobbit
        if (bookRepository.findByIsbn("9789897776090").isEmpty()) {
            Book book = new Book("9789897776090");
            bookRepository.save(book);
        }

        // 8 - Histórias de Vigaristas e Canalhas
        if (bookRepository.findByIsbn("9789896379636").isEmpty()) {
            Book book = new Book("9789896379636");
            bookRepository.save(book);
        }

        // 9 - Histórias de Aventureiros e Patifes
        if (bookRepository.findByIsbn("9789896378905").isEmpty()) {
            Book book = new Book("9789896378905");
            bookRepository.save(book);
        }

        // 10 - Windhaven
        if (bookRepository.findByIsbn("9789896375225").isEmpty()) {
            Book book = new Book("9789896375225");
            bookRepository.save(book);
        }
        // 11 - A Menina que Roubava Livros
        if (bookRepository.findByIsbn("9781101934180").isEmpty()) {
            Book book = new Book("9781101934180");
            bookRepository.save(book);
        }

        // 12 - Harry Potter e a Pedra Filosofal
        if (bookRepository.findByIsbn("9788869183157").isEmpty()) {
            Book book = new Book("9788869183157");
            bookRepository.save(book);
        }

        // 13 - Percy Jackson e o Ladrão de Raios
        if (bookRepository.findByIsbn("9788580575392").isEmpty()) {
            Book book = new Book("9788580575392");
            bookRepository.save(book);
        }

    }

    private void createLendings() {
        int i;
        int seq = 0;
        final var book1 = bookRepository.findByIsbn("9789720706386");
        final var book2 = bookRepository.findByIsbn("9789723716160");
        final var book3 = bookRepository.findByIsbn("9789895612864");
        final var book4 = bookRepository.findByIsbn("9782722203402");
        final var book5 = bookRepository.findByIsbn("9789722328296");
        final var book6 = bookRepository.findByIsbn("9789895702756");
        final var book7 = bookRepository.findByIsbn("9789897776090");
        final var book8 = bookRepository.findByIsbn("9789896379636");
        final var book9 = bookRepository.findByIsbn("9789896378905");
        final var book10 = bookRepository.findByIsbn("9789896375225");
        final var book11 = bookRepository.findByIsbn("9781101934180");
        final var book12 = bookRepository.findByIsbn("9788869183157");
        final var book13 = bookRepository.findByIsbn("9788580575392");
        List<Book> books = new ArrayList<>();
        if (book1.isPresent() && book2.isPresent()
                && book3.isPresent() && book4.isPresent()
                && book5.isPresent() && book6.isPresent()
                && book7.isPresent() && book8.isPresent()
                && book9.isPresent() && book10.isPresent()
                && book11.isPresent() && book12.isPresent()
                && book13.isPresent()) {
            books = List.of(new Book[]{book1.get(), book2.get(), book3.get(),
                    book4.get(), book5.get(), book6.get(), book7.get(),
                    book8.get(), book9.get(), book10.get(), book11.get(),
                    book12.get(), book13.get()});
        }

        final var readerDetails1 = readerRepository.findByReaderNumber("2024/1");
        final var readerDetails2 = readerRepository.findByReaderNumber("2024/2");
        final var readerDetails3 = readerRepository.findByReaderNumber("2024/3");
        final var readerDetails4 = readerRepository.findByReaderNumber("2024/4");
        final var readerDetails5 = readerRepository.findByReaderNumber("2024/5");
        final var readerDetails6 = readerRepository.findByReaderNumber("2024/6");
        final var readerDetails7 = readerRepository.findByReaderNumber("2024/7");
        final var readerDetails8 = readerRepository.findByReaderNumber("2024/8");
        final var readerDetails9 = readerRepository.findByReaderNumber("2024/9");

        List<ReaderDetails> readers = new ArrayList<>();
        if (readerDetails1.isPresent() && readerDetails2.isPresent() && readerDetails3.isPresent()
                && readerDetails4.isPresent() && readerDetails5.isPresent() && readerDetails6.isPresent()
                && readerDetails7.isPresent() && readerDetails8.isPresent() && readerDetails9.isPresent()) {
            readers = List.of(new ReaderDetails[]{readerDetails1.get(), readerDetails2.get(), readerDetails3.get(),
                    readerDetails4.get(), readerDetails5.get(), readerDetails6.get(),
                    readerDetails7.get(), readerDetails8.get(), readerDetails9.get()});
        }

        LocalDate startDate;
        LocalDate returnedDate;
        Lending lending;

        //Lendings 1 through 3 (late, returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 1, 31 - i);
                returnedDate = LocalDate.of(2024, 2, 15 + i);
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i * 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 4 through 6 (overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 3, 25 + i);
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(1 + i), readers.get(1 + i * 2), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }
        //Lendings 7 through 9 (late, overdue, not returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 4, (1 + 2 * i));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(3 / (i + 1)), readers.get(i * 2), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 10 through 12 (returned)
        for (i = 0; i < 3; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 1));
                returnedDate = LocalDate.of(2024, 5, (i + 2));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(3 - i), readers.get(1 + i * 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 13 through 18 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 2));
                returnedDate = LocalDate.of(2024, 5, (i + 2 * 2));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 19 through 23 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 8));
                returnedDate = LocalDate.of(2024, 5, (2 * i + 8));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(1 + i % 4), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 24 through 29 (returned)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 5, (i + 18));
                returnedDate = LocalDate.of(2024, 5, (2 * i + 18));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i % 2 + 2), 2024, seq, startDate, returnedDate, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 30 through 35 (not returned, not overdue)
        for (i = 0; i < 6; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (i / 3 + 1));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(i % 2 + 3), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        //Lendings 36 through 45 (not returned, not overdue)
        for (i = 0; i < 10; i++) {
            ++seq;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 6, (2 + i / 4));
                lending = Lending.newBootstrappingLending(idGenerationStrategy.generateId(), books.get(i), readers.get(4 - i % 4), 2024, seq, startDate, null, lendingDurationInDays, fineValuePerDayInCents);
                lendingRepository.save(lending);
            }
        }

        // Create lendings for book11, book12, and book13
        List<Book> newBooks = new ArrayList<>();
        List<ReaderDetails> newReaders = new ArrayList<>();
        if (book11.isPresent() && book12.isPresent() && book13.isPresent()) {
            newBooks = List.of(book11.get(), book12.get(), book13.get());
        }
        if (readerDetails7.isPresent() && readerDetails8.isPresent() && readerDetails6.isPresent()) {
            newReaders = List.of(readerDetails7.get(), readerDetails8.get(), readerDetails6.get());
        }

        for (i = 0; i < 3; i++) {
            seq++;
            if (lendingRepository.findByLendingNumber("2024/" + seq).isEmpty()) {
                startDate = LocalDate.of(2024, 7, i + 1); // Example: start dates in July
                returnedDate = LocalDate.of(2024, 7, i + 10); // Example: returned 10 days later
                lending = Lending.newBootstrappingLending(
                        idGenerationStrategy.generateId(),
                        newBooks.get(i),
                        newReaders.get(i),
                        2024,
                        seq,
                        startDate,
                        returnedDate,
                        lendingDurationInDays,
                        fineValuePerDayInCents
                );
                lendingRepository.save(lending);
            }
        }
    }

}


