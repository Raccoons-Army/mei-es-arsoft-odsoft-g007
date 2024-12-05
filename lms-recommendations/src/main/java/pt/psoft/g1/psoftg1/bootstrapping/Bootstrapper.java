package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@Order(2)
public class Bootstrapper implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void run(final String... args) throws InstantiationException {
        createBooks();
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
}


