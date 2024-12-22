package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.*;
import pt.psoft.g1.psoftg1.usermanagement.api.UserEventRabbitMQReceiver;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean(name = "booksExchange")
    public DirectExchange  booksExchange() {
        return new DirectExchange("LMS.books");
    }

    @Bean(name = "authorsExchange")
    public DirectExchange authorsExchange() {
        return new DirectExchange("LMS.authors");
    }

    @Bean(name = "genresExchange")
    public DirectExchange genresExchange() {
        return new DirectExchange("LMS.genres");
    }

    @Bean(name = "readersExchange")
    public DirectExchange readersExchange() {
        return new DirectExchange("LMS.readers");
    }

    @Bean(name = "usersExchange")
    public DirectExchange usersExchange() {
        return new DirectExchange("LMS.users");
    }

    @Bean(name = "lendingsExchange")
    public DirectExchange lendingsExchange() {
        return new DirectExchange("LMS.lendings");
    }


    private static class ReceiverConfig {

        // Books Queues
        @Bean(name = "autoDeleteQueue_Book_Created")
        public Queue autoDeleteQueue_Book_Created() {
            System.out.println("autoDeleteQueue_Book_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Book_Updated")
        public Queue autoDeleteQueue_Book_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Book_Deleted")
        public Queue autoDeleteQueue_Book_Deleted() {
            return new AnonymousQueue();
        }

        // Authors Queues
        @Bean(name = "autoDeleteQueue_Author_Created")
        public Queue autoDeleteQueue_Author_Created() {
            System.out.println("autoDeleteQueue_Author_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Author_Updated")
        public Queue autoDeleteQueue_Author_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Author_Deleted")
        public Queue autoDeleteQueue_Author_Deleted() {
            return new AnonymousQueue();
        }

        // Genres Queues
        @Bean(name = "autoDeleteQueue_Genre_Created")
        public Queue autoDeleteQueue_Genre_Created() {
            System.out.println("autoDeleteQueue_Genre_Created created!");
            return new AnonymousQueue();
        }

        // Readers Queues
        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {
            System.out.println("autoDeleteQueue_Reader_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Reader_Deleted() {
            return new AnonymousQueue();
        }

        // Users Queues
        @Bean(name = "autoDeleteQueue_User_Created")
        public Queue autoDeleteQueue_User_Created() {

            System.out.println("autoDeleteQueue_User_Created created!");
            return new AnonymousQueue();
        }

        // Lending Queues
        @Bean(name = "autoDeleteQueue_Lending_Created")
        public Queue autoDeleteQueue_Lending_Created() {
            System.out.println("autoDeleteQueue_Lending_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Updated")
        public Queue autoDeleteQueue_Lending_Updated() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Deleted")
        public Queue autoDeleteQueue_Lending_Deleted() {
            return new AnonymousQueue();
        }

        //  Book bindings
        @Bean
        public Binding bookCreatedBinding(@Qualifier("booksExchange") DirectExchange direct,
                                          @Qualifier("autoDeleteQueue_Book_Created") Queue autoDeleteQueue_Book_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Created)
                    .to(direct)
                    .with(BookEvents.BOOK_CREATED);
        }

        @Bean
        public Binding bookUpdatedBinding(@Qualifier("booksExchange") DirectExchange direct,
                                          @Qualifier("autoDeleteQueue_Book_Updated") Queue autoDeleteQueue_Book_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Updated)
                    .to(direct)
                    .with(BookEvents.BOOK_UPDATED);
        }

        @Bean
        public Binding bookDeleteBinding(@Qualifier("booksExchange") DirectExchange direct,
                                         @Qualifier("autoDeleteQueue_Book_Deleted") Queue autoDeleteQueue_Book_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Book_Deleted)
                    .to(direct)
                    .with(BookEvents.BOOK_DELETED);
        }

        // Author bindings
        @Bean
        public Binding authorCreatedBinding(@Qualifier("authorsExchange") DirectExchange direct,
                                            @Qualifier("autoDeleteQueue_Author_Created") Queue autoDeleteQueue_Author_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Created)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_CREATED);
        }

        @Bean
        public Binding authorUpdatedBinding(@Qualifier("authorsExchange") DirectExchange direct,
                                            @Qualifier("autoDeleteQueue_Author_Updated") Queue autoDeleteQueue_Author_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Updated)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_UPDATED);
        }

        @Bean
        public Binding authorDeleteBinding(@Qualifier("authorsExchange") DirectExchange direct,
                                           @Qualifier("autoDeleteQueue_Author_Deleted") Queue autoDeleteQueue_Author_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Deleted)
                    .to(direct)
                    .with(AuthorEvents.AUTHOR_DELETED);
        }

        // Genre bindings
        @Bean
        public Binding genreCreatedBinding(@Qualifier("genresExchange") DirectExchange direct,
                                           @Qualifier("autoDeleteQueue_Genre_Created") Queue autoDeleteQueue_Genre_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Created)
                    .to(direct)
                    .with(GenreEvents.GENRE_CREATED);
        }

        // Reader bindings
        @Bean
        public Binding readerCreatedBinding(@Qualifier("readersExchange") DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Created)
                    .to(direct)
                    .with(ReaderEvents.READER_CREATED);
        }

        @Bean
        public Binding readerDeletedBinding(@Qualifier("readersExchange") DirectExchange direct,
                                Queue autoDeleteQueue_Reader_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Deleted)
                    .to(direct)
                    .with(ReaderEvents.READER_DELETED);
        }

        // User bindings
        @Bean
        public Binding userCreatedBinding(@Qualifier("usersExchange") DirectExchange direct,
                                Queue autoDeleteQueue_User_Created) {
            return BindingBuilder.bind(autoDeleteQueue_User_Created)
                    .to(direct)
                    .with(UserEvents.USER_CREATED);
        }

        // Lending Bindings
        @Bean
        public Binding lendingCreatedBinding(@Qualifier("lendingsExchange") DirectExchange direct,
                                             @Qualifier("autoDeleteQueue_Lending_Created") Queue autoDeleteQueue_Lending_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Created)
                    .to(direct)
                    .with(LendingEvents.LENDING_CREATED);
        }

        @Bean
        public Binding lendingUpdatedBinding(@Qualifier("lendingsExchange") DirectExchange lendingExchange,
                                             @Qualifier("autoDeleteQueue_Lending_Updated") Queue autoDeleteQueue_Lending_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Updated)
                    .to(lendingExchange)
                    .with(LendingEvents.LENDING_UPDATED);
        }

        @Bean
        public Binding lendingDeletedBinding(@Qualifier("lendingsExchange") DirectExchange lendingExchange,
                                             @Qualifier("autoDeleteQueue_Lending_Deleted") Queue autoDeleteQueue_Lending_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Deleted)
                    .to(lendingExchange)
                    .with(LendingEvents.LENDING_DELETED);
        }


        //  Receivers
        @Bean
        public BookEventRabbitmqReceiver receiver(BookService bookService) {
            return new BookEventRabbitmqReceiver(bookService);
        }

        @Bean
        public AuthorEventRabbitmqReceiver authorReceiver(AuthorService authorService) {
            return new AuthorEventRabbitmqReceiver(authorService);
        }

        @Bean
        public GenreEventRabbitmqReceiver genreReceiver(GenreService genreService) {
            return new GenreEventRabbitmqReceiver(genreService);
        }

        @Bean
        public ReaderEventRabbitMQReceiver readerReceiver(ReaderService readerService) {
            return new ReaderEventRabbitMQReceiver(readerService);
        }

        @Bean
        public UserEventRabbitMQReceiver userReceiver(UserService userService) {
            return new UserEventRabbitMQReceiver(userService);
        }

        @Bean
        public LendingEventRabbitmqReceiver lendingReceiver(LendingService lendingService) {
            return new LendingEventRabbitmqReceiver(lendingService);
        }

    }
}
